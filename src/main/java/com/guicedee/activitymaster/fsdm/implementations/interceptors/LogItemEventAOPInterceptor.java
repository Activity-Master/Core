package com.guicedee.activitymaster.fsdm.implementations.interceptors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.annotations.LogItem;
import com.guicedee.activitymaster.fsdm.client.services.annotations.LogItemTypes;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.*;
import com.guicedee.guicedinjection.pairing.Pair;
import com.guicedee.services.jsonrepresentation.IJsonRepresentation;
import com.guicedee.services.xmlrepresentation.IXmlRepresentation;
import lombok.extern.java.Log;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
	
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable
	{
		if (event == null)
		{
			com.guicedee.client.IGuiceContext.instance().inject()
			            .injectMembers(this);
		}
		if (!configuration.isEnterpriseReady() || getISystem(ActivityMasterSystemName) == null)
		{
			return methodInvocation.proceed();
		}
		if (event == null || event.getId() == null)
		{
			return methodInvocation.proceed();
		}
		
		var refObject = getRefObject(methodInvocation);
		for (Pair<LogItem, Object> pair : refObject)
		{
			if (pair.getValue() == null)
			{
				pair.setValue("null");
			}
			checkClassificationExists(pair.getKey().value());
			processLogItemEntry(pair);
		}
		return methodInvocation.proceed();
		
	}
	
	private void checkClassificationExists(String classificationName)
	{
		try
		{
			classificationService.find(classificationName, getISystem(ActivityMasterSystemName), getISystemToken(ActivityMasterSystemName));
		}catch (ClassificationException e)
		{
			classificationService.create(classificationName, classificationName, EventXAddress, getISystem(ActivityMasterSystemName), 0, "LogItemTypes", getISystemToken(ActivityMasterSystemName));
		}
	}
	
	
	public void processLogItemEntry(Pair<LogItem, Object> logItemObjectPair) throws ExecutionException, InterruptedException {
		Pair<Object, LogItemTypes> of = Pair.of(logItemObjectPair.getValue(), logItemObjectPair.getKey()
		                                                                                       .type());
		byte[] bytes = decodeReferenceObject(of);
		IResourceItem<?, ?> resourceItem = resourceItemService.create("LogItem", logItemObjectPair.getKey()
		                                                                                          .type()
		                                                                                          .getMimeType(), getISystem(ActivityMasterSystemName) , getISystemToken(ActivityMasterSystemName) )
				.get();
		resourceItem.updateData(bytes, getISystem(ActivityMasterSystemName) , getISystemToken(ActivityMasterSystemName) );
		event.addResourceItem(logItemObjectPair.getKey()
		                                       .value(), resourceItem,
				logItemObjectPair.getKey()
				                 .value()
				, getISystem(ActivityMasterSystemName), getISystemToken(ActivityMasterSystemName) );
		
	}
	
	private byte[] decodeReferenceObject(Pair<Object, LogItemTypes> pair)
	{
		Object key = pair.getKey();
		if (key instanceof IResourceItem)
		{
			IResourceItem<?, ?> resourceItem = (IResourceItem<?, ?>) key;
			return resourceItem.getData();
		}
		if (key instanceof String)
		{
			return key.toString()
			          .getBytes();
		}
		
		switch (pair.getValue())
		{
			case Xml:
				if (key instanceof IXmlRepresentation<?>)
				{
					IXmlRepresentation<?> xml = (IXmlRepresentation<?>) key;
					return xml.toXml()
					          .getBytes();
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
					return (byte[]) key;
				}
				throw new ResourceItemException("Log Item Type is not a byte[] for the specified type - " + pair.getValue());
			}
			case CSV:
			case CSS:
			case TLDCSV:
			case JavaScript:
			case Text:
				return key.toString()
				          .getBytes();
			case Json:
			default:
				if (key instanceof IJsonRepresentation<?>)
				{
					IJsonRepresentation<?> rep = (IJsonRepresentation<?>) key;
					return rep.toJson()
					          .getBytes();
				}
				try
				{
					return com.guicedee.client.IGuiceContext.get(DefaultObjectMapper)
					                   .writerWithDefaultPrettyPrinter()
					                   .writeValueAsBytes(key);
				}
				catch (JsonProcessingException e)
				{
					log.log(Level.SEVERE, "Unable to decode LogEventItem to JSON", e);
					break;
				}
		}
		return new byte[]{};
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
