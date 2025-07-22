package com.guicedee.activitymaster.fsdm.implementations.interceptors;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.ReactiveTransactionUtil;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.annotations.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangement;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ClassificationException;
import com.guicedee.guicedinjection.pairing.Pair;
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

@Log
public class ArrangementEventAOPInterceptor implements MethodInterceptor
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

	@Inject
	private IEnterpriseService<?> enterpriseService;

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable
	{
		if (event == null)
		{
			com.guicedee.client.IGuiceContext.instance().inject().injectMembers(this);
		}

		// Check if enterprise is ready
/*  if (!configuration.isEnterpriseReady())
		{
			return methodInvocation.proceed();
		}*/

		// Check if event ID is null
		if (event.getId() == null)
		{
			return methodInvocation.proceed();
		}

		// Get the arrangement event annotation
		ArrangementEvent arrangementEvent = methodInvocation.getMethod()
		                                         .getAnnotation(ArrangementEvent.class);

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
					// Process each arrangement in parallel
					for (Pair<Arrangement, IArrangement<?, ?>> pair : refObject)
					{
						if (pair.getValue() == null)
						{
							continue;
						}

						String classification = pair.getKey().value();

						// Create an operation for each arrangement
						Uni<?> operation = checkClassificationExists(classification, enterprise)
							.chain(result -> {
								// Get system and token
								return getISystem(ActivityMasterSystemName, enterprise)
									.chain(system -> {
										return getISystemToken(ActivityMasterSystemName, enterprise)
											.chain(token -> {
												// Add arrangement to event
												return Uni.createFrom().item(() -> {
													this.event.addArrangement(pair.getValue(), classification, arrangementEvent.classificationName(), system, token);
													return null;
												});
											});
									});
							})
							.onFailure().invoke(error -> log.log(Level.SEVERE, "Error processing arrangement: " + classification, error));

						// Add operation to list
						operations.add(operation);
					}

					// Run all operations in parallel
					if (!operations.isEmpty()) {
						Uni.combine().all().unis(operations)
							.discardItems()
							.subscribe().with(
								result -> log.info("All arrangement operations completed successfully"),
								error -> log.log(Level.SEVERE, "Error in parallel arrangement operations", error)
							);
					}
				},
				error -> log.log(Level.SEVERE, "Error getting enterprise: " + error.getMessage(), error)
			);

		// Proceed with method invocation
		Object result = null;
		try
		{
			result = methodInvocation.proceed();
		}
		catch (Throwable t)
		{
			throw t;
		}

		// Process log item if present
		if (methodInvocation.getMethod().isAnnotationPresent(LogItem.class))
		{
			LogItemEventAOPInterceptor logItemEventAOPInterceptor = com.guicedee.client.IGuiceContext.get(LogItemEventAOPInterceptor.class);
			final Object finalResult = result; // Create a final copy of the result
			enterpriseService.getEnterprise(enterpriseName)
				.subscribe().with(
					enterprise -> {
						logItemEventAOPInterceptor.processLogItemEntry(Pair.of(methodInvocation.getMethod().getAnnotation(LogItem.class), finalResult), enterprise)
							.subscribe().with(
								success -> log.info("Log item processed successfully"),
								error -> log.log(Level.SEVERE, "Error processing log item", error)
							);
					},
					error -> log.log(Level.SEVERE, "Error getting enterprise for log item", error)
				);
		}

		return result;
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
									classificationService.create(classificationName, classificationName, EventXArrangement, system, 0, "LogItemTypes", token);
									return null;
								}
							});
						});
				});
		});
	}

	private List<Pair<Arrangement, IArrangement<?, ?>>> getRefObject(MethodInvocation methodInvocation)
	{
		List<Pair<Arrangement, IArrangement<?, ?>>> output = new ArrayList<>();


		Parameter[] parameters = methodInvocation.getMethod()
		                                         .getParameters();
		for (int i = 0; i < parameters.length; i++)
		{
			Parameter parameter = parameters[i];
			if (parameter.isAnnotationPresent(Arrangement.class))
			{
				output.add(Pair.of(parameter.getAnnotation(Arrangement.class), (IArrangement<?, ?>) methodInvocation.getArguments()[i]));
			}
		}
		if (output.isEmpty())
		{
			throw new ActivityMasterException("Unable to find any parameter with @Arrangement to use for listener");
		}
		return output;
	}
}
