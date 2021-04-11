package com.guicedee.activitymaster.fsdm.implementations.interceptors;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.annotations.Classification;
import com.guicedee.activitymaster.fsdm.client.services.annotations.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ClassificationException;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.pairing.Pair;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import static com.guicedee.activitymaster.fsdm.client.services.ISystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.*;
import static com.guicedee.activitymaster.fsdm.implementations.interceptors.EventsAOPInterceptor.*;

public class ClassificationEventAOPInterceptor implements MethodInterceptor
{
	@Inject
	private ActivityMasterConfiguration configuration;
	@Inject
	private IClassificationService<?> classificationService;
	/**
	 * The running event
	 */
	@Inject
	private IEvent<?,?> event;
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable
	{
		if (event == null)
		{
			GuiceContext.inject().injectMembers(this);
		}
		if (!configuration.isEnterpriseReady() || getISystem(ActivityMasterSystemName) == null)
		{
			return methodInvocation.proceed();
		}
		if (event.getId() == null)
		{
			return methodInvocation.proceed();
		}
		ClassificationEvent event = methodInvocation.getMethod()
		                                            .getAnnotation(ClassificationEvent.class);
		
		var refObject
				= getRefObject(methodInvocation);
		
		for (Pair<Classification, IClassification<?, ?>> pair : refObject)
		{
			String classification = pair.getValue()
			                            .getName();
			checkClassificationExists(classification);
			this.event.addClassification(classification,EventXClassification,event.classificationName(), getISystem(ActivityMasterSystemName), getISystemToken(ActivityMasterSystemName));
		}
		
		Object o = null;
		try
		{
			o = methodInvocation.proceed();
		}catch (Throwable t)
		{
			throw t;
		}
		if (methodInvocation.getMethod()
		                    .isAnnotationPresent(LogItem.class))
		{
			LogItemEventAOPInterceptor logItemEventAOPInterceptor = GuiceContext.get(LogItemEventAOPInterceptor.class);
			logItemEventAOPInterceptor.processLogItemEntry(Pair.of(methodInvocation.getMethod().getAnnotation(LogItem.class),o));
		}
		return o;
	}
	
	private void checkClassificationExists(String classificationName)
	{
		try
		{
			classificationService.find(classificationName, getISystem(ActivityMasterSystemName), getISystemToken(ActivityMasterSystemName));
		}catch (ClassificationException e)
		{
			classificationService.create(classificationName, classificationName, EventXArrangement, getISystem(ActivityMasterSystemName), 0, "LogItemTypes", getISystemToken(ActivityMasterSystemName));
		}
	}
	
	private List<Pair<Classification, IClassification<?, ?>>> getRefObject(MethodInvocation methodInvocation)
	{
		List<Pair<Classification, IClassification<?, ?>>> output = new ArrayList<>();
		
		
		Parameter[] parameters = methodInvocation.getMethod()
		                                         .getParameters();
		for (int i = 0; i < parameters.length; i++)
		{
			Parameter parameter = parameters[i];
			if (parameter.isAnnotationPresent(Classification.class))
			{
				output.add(Pair.of(parameter.getAnnotation(Classification.class), (IClassification<?, ?>) methodInvocation.getArguments()[i]));
			}
		}
		if (output.isEmpty())
		{
			throw new ActivityMasterException("Unable to find any parameter with @Classification to use for listener");
		}
		return output;
	}
}
