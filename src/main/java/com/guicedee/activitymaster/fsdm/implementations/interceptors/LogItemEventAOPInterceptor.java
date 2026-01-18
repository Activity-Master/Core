package com.guicedee.activitymaster.fsdm.implementations.interceptors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.annotations.LogItem;
import com.guicedee.activitymaster.fsdm.client.services.annotations.LogItemTypes;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.*;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.utils.Pair;
import com.guicedee.services.jsonrepresentation.IJsonRepresentation;
import com.guicedee.services.xmlrepresentation.IXmlRepresentation;
import io.smallrye.mutiny.Uni;
import lombok.extern.java.Log;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.reactive.mutiny.Mutiny;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.*;
import static com.guicedee.client.implementations.ObjectBinderKeys.*;
import static com.guicedee.client.IGuiceContext.get;

@Log
public class LogItemEventAOPInterceptor implements MethodInterceptor
{
	@Inject
	private ActivityMasterConfiguration configuration;
	
	@Inject
	private IResourceItemService<?> resourceItemService;
	@Inject
	private IClassificationService<?> classificationService;
	@Inject
	private IEnterpriseService<?> enterpriseService;
	
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable
	{
		IEvent<?, ?> currentEvent = EventsAOPInterceptor.getCurrentEvent();
		
		if (configuration.getApplicationEnterpriseName() == null || currentEvent == null || currentEvent.getId() == null)
		{
			return methodInvocation.proceed();
		}

		Object result = methodInvocation.proceed();

		if (result instanceof Uni)
		{
			return ((Uni<?>) result).onItem().call(res -> processLogItems(methodInvocation, currentEvent, res));
		}
		else
		{
			processLogItems(methodInvocation, currentEvent, result)
				.subscribe().with(
					item -> log.info("Log item processed successfully"),
					error -> log.log(Level.SEVERE, "Error processing log items", error)
				);
			return result;
		}
	}

	private Uni<Void> processLogItems(MethodInvocation methodInvocation, IEvent<?, ?> currentEvent, Object result)
	{
		Mutiny.SessionFactory sessionFactory = get(Mutiny.SessionFactory.class);
		String enterpriseName = configuration.getApplicationEnterpriseName();
		
		return sessionFactory.withTransaction(session -> {
			return enterpriseService.getEnterprise(session, enterpriseName)
				.chain(enterprise -> {
					List<Uni<?>> operations = new ArrayList<>();
					
					// Parameters
					var refObject = getRefObject(methodInvocation);
					for (Pair<LogItem, Object> pair : refObject)
					{
						operations.add(processLogItemEntry(session, currentEvent, pair, enterprise));
					}
					
					// Method result
					if (methodInvocation.getMethod().isAnnotationPresent(LogItem.class))
					{
						LogItem logItem = methodInvocation.getMethod().getAnnotation(LogItem.class);
						operations.add(processLogItemEntry(session, currentEvent, Pair.of(logItem, result), enterprise));
					}
					
					if (operations.isEmpty()) return Uni.createFrom().voidItem();
					return Uni.combine().all().unis(operations).discardItems();
				});
		});
	}
	
	private Uni<Void> checkClassificationExists(Mutiny.Session session, String classificationName, IEnterprise<?, ?> enterprise)
	{
		return getISystem(session, ActivityMasterSystemName, enterprise)
			.chain(system -> getISystemToken(session, ActivityMasterSystemName, enterprise)
				.chain(token -> {
					return classificationService.find(session, classificationName, system, token)
						.onFailure().recoverWithUni(() -> classificationService.create(session, classificationName, classificationName, EventXAddress, system, 0, "LogItemTypes", token))
						.replaceWith(Uni.createFrom().voidItem());
				}));
	}
	
	public Uni<Void> processLogItemEntry(Mutiny.Session session, IEvent<?, ?> currentEvent, Pair<LogItem, Object> logItemObjectPair, IEnterprise<?, ?> enterprise) {
		Object value = logItemObjectPair.getValue();
		if (value == null) value = "null";
		
		Pair<Object, LogItemTypes> of = Pair.of(value, logItemObjectPair.getKey().type());
		String classification = logItemObjectPair.getKey().value();

		return checkClassificationExists(session, classification, enterprise)
			.chain(() -> decodeReferenceObject(session, of))
			.chain(bytes -> getISystem(session, ActivityMasterSystemName, enterprise)
				.chain(system -> getISystemToken(session, ActivityMasterSystemName, enterprise)
					.chain(token -> resourceItemService.create(session, "LogItem", logItemObjectPair.getKey().type().getMimeType(), bytes, system, token)
						.chain(rih -> currentEvent.addResourceItem(session, classification, rih, classification, system, token))
						.replaceWith(Uni.createFrom().voidItem()))));
	}
	
	private Uni<byte[]> decodeReferenceObject(Mutiny.Session session, Pair<Object, LogItemTypes> pair)
	{
		Object key = pair.getKey();
		if (key instanceof IResourceItem)
		{
			IResourceItem<?, ?> resourceItem = (IResourceItem<?, ?>) key;
			return resourceItem.getData(session);
		}
		if (key instanceof String)
		{
			return Uni.createFrom().item(key.toString().getBytes());
		}
		
		switch (pair.getValue())
		{
			case Xml:
				if (key instanceof IXmlRepresentation<?>)
				{
					IXmlRepresentation<?> xml = (IXmlRepresentation<?>) key;
					return Uni.createFrom().item(xml.toXml().getBytes());
				}
				break;
			case Excel:
			case ExcelX:
			case Word:
			case WordX:
			case Powerpoint:
			case PowerpointX:
			case PDF:
			case ZIP:
			{
				if (key instanceof byte[])
				{
					return Uni.createFrom().item((byte[]) key);
				}
				return Uni.createFrom().failure(new ResourceItemException("Log Item Type is not a byte[] for the specified type - " + pair.getValue()));
			}
			case CSV:
			case CSS:
			case TLDCSV:
			case JavaScript:
			case Text:
				return Uni.createFrom().item(key.toString().getBytes());
			case Json:
			default:
				if (key instanceof IJsonRepresentation<?>)
				{
					IJsonRepresentation<?> rep = (IJsonRepresentation<?>) key;
					return Uni.createFrom().item(rep.toJson().getBytes());
				}
				try
				{
					return Uni.createFrom().item(IGuiceContext.get(DefaultObjectMapper)
					                   .writerWithDefaultPrettyPrinter()
					                   .writeValueAsBytes(key));
				}
				catch (JsonProcessingException e)
				{
					log.log(Level.SEVERE, "Unable to decode LogEventItem to JSON", e);
					return Uni.createFrom().failure(e);
				}
		}
		return Uni.createFrom().item(new byte[]{});
	}
	
	private List<Pair<LogItem, Object>> getRefObject(MethodInvocation methodInvocation)
	{
		List<Pair<LogItem, Object>> output = new ArrayList<>();
		
		Parameter[] parameters = methodInvocation.getMethod().getParameters();
		for (int i = 0; i < parameters.length; i++)
		{
			Parameter parameter = parameters[i];
			if (parameter.isAnnotationPresent(LogItem.class))
			{
				output.add(Pair.of(parameter.getAnnotation(LogItem.class), methodInvocation.getArguments()[i]));
			}
		}
		return output;
	}
}