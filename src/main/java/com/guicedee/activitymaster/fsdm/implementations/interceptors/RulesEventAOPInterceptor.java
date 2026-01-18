package com.guicedee.activitymaster.fsdm.implementations.interceptors;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.annotations.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRules;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ClassificationException;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.utils.Pair;
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
import static com.guicedee.client.IGuiceContext.get;

@Log
public class RulesEventAOPInterceptor implements MethodInterceptor
{
	@Inject
	private ActivityMasterConfiguration configuration;
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
			return ((Uni<?>) result).onItem().call(() -> processRules(methodInvocation, currentEvent));
		}
		else
		{
			processRules(methodInvocation, currentEvent)
				.subscribe().with(
					item -> log.info("Rules processed successfully"),
					error -> log.log(Level.SEVERE, "Error processing rules", error)
				);
			return result;
		}
	}

	private Uni<Void> processRules(MethodInvocation methodInvocation, IEvent<?, ?> currentEvent)
	{
		Mutiny.SessionFactory sessionFactory = get(Mutiny.SessionFactory.class);
		String enterpriseName = configuration.getApplicationEnterpriseName();
		RulesEvent rulesEvent = methodInvocation.getMethod().getAnnotation(RulesEvent.class);
		var refObject = getRefObject(methodInvocation);

		return sessionFactory.withTransaction(session -> {
			return enterpriseService.getEnterprise(session, enterpriseName)
				.chain(enterprise -> {
					List<Uni<?>> operations = new ArrayList<>();
					for (Pair<RuleSet, IRules<?, ?>> pair : refObject)
					{
						if (pair.getValue() == null) continue;
						String classification = pair.getKey().value();
						
						operations.add(checkClassificationExists(session, classification, enterprise)
							.chain(() -> getISystem(session, ActivityMasterSystemName, enterprise))
							.chain(system -> getISystemToken(session, ActivityMasterSystemName, enterprise)
								.chain(token -> currentEvent.addRules(session, pair.getValue(), classification, rulesEvent.classificationName(), system, token)))
							.onFailure().invoke(error -> log.log(Level.SEVERE, "Error processing rules: " + classification, error))
						);
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
						.onFailure().recoverWithUni(() -> classificationService.create(session, classificationName, classificationName, EventXRules, system, 0, "LogItemTypes", token))
						.replaceWith(Uni.createFrom().voidItem());
				}));
	}

	private List<Pair<RuleSet, IRules<?, ?>>> getRefObject(MethodInvocation methodInvocation)
	{
		List<Pair<RuleSet, IRules<?, ?>>> output = new ArrayList<>();
		Parameter[] parameters = methodInvocation.getMethod().getParameters();
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
