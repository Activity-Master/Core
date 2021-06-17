package com.guicedee.activitymaster.fsdm.implementations.interceptors;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.annotations.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRules;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ClassificationException;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.pairing.Pair;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.*;

public class RulesEventAOPInterceptor implements MethodInterceptor
{
	@Inject
	private ActivityMasterConfiguration configuration;
	/**
	 * The running event
	 */
	@Inject
	private IEvent<?, ?> event;
	@Inject
	private IClassificationService<?> classificationService;
	
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable
	{
		if (event == null)
		{
			GuiceContext.inject()
			            .injectMembers(this);
		}
		if (!configuration.isEnterpriseReady() || getISystem(ActivityMasterSystemName) == null)
		{
			return methodInvocation.proceed();
		}
		if (event.getId() == null)
		{
			return methodInvocation.proceed();
		}
		RulesEvent event = methodInvocation.getMethod()
		                                   .getAnnotation(RulesEvent.class);
		
		var refObject
				= getRefObject(methodInvocation);
		
		for (Pair<RuleSet, IRules<?, ?>> pair : refObject)
		{
			if (pair.getValue() == null)
			{
				continue;
			}
			String classification = pair.getKey()
			                            .value();
			checkClassificationExists(classification);
			this.event.addRules(pair.getValue(), classification, event.classificationName(), getISystem(ActivityMasterSystemName), getISystemToken(ActivityMasterSystemName));
		}
		
		Object o = null;
		try
		{
			o = methodInvocation.proceed();
		}
		catch (Throwable t)
		{
			throw t;
		}
		if (methodInvocation.getMethod()
		                    .isAnnotationPresent(LogItem.class))
		{
			LogItemEventAOPInterceptor logItemEventAOPInterceptor = GuiceContext.get(LogItemEventAOPInterceptor.class);
			logItemEventAOPInterceptor.processLogItemEntry(Pair.of(methodInvocation.getMethod()
			                                                                       .getAnnotation(LogItem.class), o));
		}
		return o;
	}
	
	private void checkClassificationExists(String classificationName)
	{
		try
		{
			classificationService.find(classificationName, getISystem(ActivityMasterSystemName), getISystemToken(ActivityMasterSystemName));
		}
		catch (ClassificationException e)
		{
			classificationService.create(classificationName, classificationName, EventXRules, getISystem(ActivityMasterSystemName), 0, "LogItemTypes", getISystemToken(ActivityMasterSystemName));
		}
	}
	
	private List<Pair<RuleSet, IRules<?, ?>>> getRefObject(MethodInvocation methodInvocation)
	{
		List<Pair<RuleSet, IRules<?, ?>>> output = new ArrayList<>();
		Parameter[] parameters = methodInvocation.getMethod()
		                                         .getParameters();
		for (int i = 0; i < parameters.length; i++)
		{
			Parameter parameter = parameters[i];
			if (parameter.isAnnotationPresent(RuleSet.class))
			{
				output.add(Pair.of(parameter.getAnnotation(RuleSet.class), (IRules<?, ?>) methodInvocation.getArguments()[i]));
			}
		}
		if (output.isEmpty())
		{
			throw new ActivityMasterException("Unable to find any parameter with @Rules to use for listener");
		}
		return output;
	}
}
