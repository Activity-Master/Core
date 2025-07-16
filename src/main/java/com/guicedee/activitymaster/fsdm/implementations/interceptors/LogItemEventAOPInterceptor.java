package com.guicedee.activitymaster.fsdm.implementations.interceptors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.ReactiveTransactionUtil;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.annotations.LogItem;
import com.guicedee.activitymaster.fsdm.client.services.annotations.LogItemTypes;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.*;
import com.guicedee.guicedinjection.pairing.Pair;
import com.guicedee.services.jsonrepresentation.IJsonRepresentation;
import com.guicedee.services.xmlrepresentation.IXmlRepresentation;
import io.smallrye.mutiny.Uni;
import lombok.extern.java.Log;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.*;
import static com.guicedee.guicedinjection.interfaces.ObjectBinderKeys.*;

@Log
public class LogItemEventAOPInterceptor implements MethodInterceptor
{
	@Inject
	private ActivityMasterConfiguration configuration;
	/**
	 * The running event
	 */
	@Inject
	private IEvent<?, ?> event;
	
	@Inject
	private IResourceItemService<?> resourceItemService;
	@Inject
	private IClassificationService<?> classificationService;
	@Inject
	private IEnterpriseService<?> enterpriseService;
	
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable
	{
		if (event == null)
		{
			com.guicedee.client.IGuiceContext.instance().inject()
			            .injectMembers(this);
		}
		
		// Check if enterprise is ready
		if (!configuration.isEnterpriseReady())
		{
			return methodInvocation.proceed();
		}
		
		// Check if event ID is null
		if (event == null || event.getId() == null)
		{
			return methodInvocation.proceed();
		}
		
		// Get reference objects
		var refObject = getRefObject(methodInvocation);
		
		// Get enterprise from configuration
		String enterpriseName = configuration.getApplicationEnterpriseName();
		
		// Create a list to hold all operations
		List<Uni<?>> operations = new ArrayList<>();
		
		// Get enterprise from name
		enterpriseService.getEnterprise(enterpriseName)
			.subscribe().with(
				enterprise -> {
					// Process each log item in parallel
					for (Pair<LogItem, Object> pair : refObject)
					{
						if (pair.getValue() == null)
						{
							pair.setValue("null");
						}
						
						String classification = pair.getKey().value();
						
						// Create an operation for each log item
						Uni<?> operation = checkClassificationExists(classification, enterprise)
							.chain(result -> {
								try {
									return processLogItemEntry(pair, enterprise);
								} catch (Exception e) {
									log.log(Level.SEVERE, "Error processing log item: " + classification, e);
									return Uni.createFrom().failure(e);
								}
							})
							.onFailure().invoke(error -> log.log(Level.SEVERE, "Error processing log item: " + classification, error));
						
						// Add operation to list
						operations.add(operation);
					}
					
					// Run all operations in parallel
					if (!operations.isEmpty()) {
						Uni.combine().all().unis(operations)
							.discardItems()
							.subscribe().with(
								result -> log.info("All log item operations completed successfully"),
								error -> log.log(Level.SEVERE, "Error in parallel log item operations", error)
							);
					}
				},
				error -> log.log(Level.SEVERE, "Error getting enterprise: " + error.getMessage(), error)
			);
		
		// Proceed with method invocation
		return methodInvocation.proceed();
	}
	
	private Uni<Void> checkClassificationExists(String classificationName, IEnterprise<?,?> enterprise)
	{
		return ReactiveTransactionUtil.withTransaction(session -> {
			return getISystem(ActivityMasterSystemName, enterprise)
				.chain(system -> {
					return getISystemToken(ActivityMasterSystemName, enterprise)
						.chain(token -> {
							// Try to find the classification
							return Uni.createFrom().item(() -> {
								try {
									classificationService.find(classificationName, system, token);
									return null;
								} catch (ClassificationException e) {
									// Create the classification if it doesn't exist
									classificationService.create(classificationName, classificationName, EventXAddress, system, 0, "LogItemTypes", token);
									return null;
								}
							});
						});
				});
		});
	}
	
	public Uni<Void> processLogItemEntry(Pair<LogItem, Object> logItemObjectPair, IEnterprise<?,?> enterprise) {
		return ReactiveTransactionUtil.withTransaction(session -> {
			Pair<Object, LogItemTypes> of = Pair.of(logItemObjectPair.getValue(), logItemObjectPair.getKey().type());
			
			return decodeReferenceObject(of)
				.chain(bytes -> {
					return getISystem(ActivityMasterSystemName, enterprise)
						.chain(system -> {
							return getISystemToken(ActivityMasterSystemName, enterprise)
								.chain(token -> {
									return resourceItemService.create("LogItem", logItemObjectPair.getKey().type().getMimeType(), bytes, system, token)
										.chain(rih -> {
											return Uni.createFrom().item(() -> {
												event.addResourceItem(logItemObjectPair.getKey().value(), rih, logItemObjectPair.getKey().value(), system, token);
												return null;
											});
										});
								});
						});
				});
		});
	}
	
	private Uni<byte[]> decodeReferenceObject(Pair<Object, LogItemTypes> pair)
	{
		Object key = pair.getKey();
		if (key instanceof IResourceItem)
		{
			IResourceItem<?, ?> resourceItem = (IResourceItem<?, ?>) key;
			return resourceItem.getData();
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
					return Uni.createFrom().item(com.guicedee.client.IGuiceContext.get(DefaultObjectMapper)
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
	
	/**
	 * @param methodInvocation
	 * @return The mimetype with the object as the key
	 */
	private List<Pair<LogItem, Object>> getRefObject(MethodInvocation methodInvocation)
	{
		List<Pair<LogItem, Object>> output = new ArrayList<>();
		
		Parameter[] parameters = methodInvocation.getMethod()
		                                         .getParameters();
		for (int i = 0; i < parameters.length; i++)
		{
			Parameter parameter = parameters[i];
			if (parameter.isAnnotationPresent(LogItem.class))
			{
				output.add(Pair.of(parameter.getAnnotation(LogItem.class), methodInvocation.getArguments()[i]));
			}
		}
		if (output.isEmpty())
		{
			throw new ActivityMasterException("Unable to find any parameter with @LogItem to use for listener");
		}
		return output;
	}
}