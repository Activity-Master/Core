package com.guicedee.activitymaster.fsdm.implementations.interceptors;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.annotations.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ClassificationException;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.pairing.Pair;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import static com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService.*;
import static com.guicedee.activitymaster.fsdm.client.services.ISystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.*;

public class InvolvedPartyEventAOPInterceptor implements MethodInterceptor
{
	@Inject
	private ActivityMasterConfiguration configuration;
	/**
	 * The running event
	 */
	@Inject
	private IEvent<?,?> event;
	@Inject
	private IClassificationService<?> classificationService;

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

		InvolvedPartyEvent event = methodInvocation.getMethod()
		                                           .getAnnotation(InvolvedPartyEvent.class);
		
		var refObject
				= getRefObject(methodInvocation);
		
		for (Pair<Party, IInvolvedParty<?, ?>> pair : refObject)
		{
			if (pair.getValue() == null)
			{
				continue;
			}
			String classification = pair.getKey()
			                            .value();
			checkClassificationExists(classification);
			this.event.addInvolvedParty(pair.getValue(), classification, event.classificationName(), getISystem(ActivityMasterSystemName), getISystemToken(ActivityMasterSystemName));
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
			classificationService.create(classificationName, classificationName, EventXInvolvedParty, getISystem(ActivityMasterSystemName), 0, "LogItemTypes", getISystemToken(ActivityMasterSystemName));
		}
	}
	
	private List<Pair<Party, IInvolvedParty<?, ?>>> getRefObject(MethodInvocation methodInvocation)
	{
		List<Pair<Party, IInvolvedParty<?, ?>>> output = new ArrayList<>();
		
		
		Parameter[] parameters = methodInvocation.getMethod()
		                                         .getParameters();
		for (int i = 0; i < parameters.length; i++)
		{
			Parameter parameter = parameters[i];
			if (parameter.isAnnotationPresent(Party.class))
			{
				Object argument = methodInvocation.getArguments()[i];
				if(argument instanceof IInvolvedParty)
				{
					output.add(Pair.of(parameter.getAnnotation(Party.class), (IInvolvedParty<?, ?>) argument));
				}
				else if(argument instanceof ISystems)
				{
					ISystems<?, ?> sys = (ISystems<?, ?>) argument;
					String name = sys.getName();
					
				}
			}
		}
		if (output.isEmpty())
		{
			throw new ActivityMasterException("Unable to find any parameter with @Party to use for listener");
		}
		return output;
	}
}
