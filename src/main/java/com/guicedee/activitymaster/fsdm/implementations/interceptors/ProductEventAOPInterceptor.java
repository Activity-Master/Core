package com.guicedee.activitymaster.fsdm.implementations.interceptors;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.annotations.ProductEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.types.annotations.LogItem;
import com.guicedee.activitymaster.fsdm.client.types.annotations.Product;
import com.guicedee.activitymaster.fsdm.client.types.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.fsdm.client.types.exceptions.ClassificationException;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.pairing.Pair;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Parameter;
import java.util.*;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService.*;
import static com.guicedee.activitymaster.fsdm.client.types.classifications.EnterpriseClassificationDataConcepts.*;

public class ProductEventAOPInterceptor implements MethodInterceptor
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
	
	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> system;
	@Inject
	@Named(ActivityMasterSystemName)
	private UUID identityToken;
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable
	{
		if (system == null)
		{
			GuiceContext.inject().injectMembers(this);
		}
		if (!configuration.isEnterpriseReady() || system.isFake())
		{
			return methodInvocation.proceed();
		}
		if (event.getId() == null)
		{
			return methodInvocation.proceed();
		}
	
		ProductEvent event = methodInvocation.getMethod()
		                                     .getAnnotation(ProductEvent.class);
		
		var refObject
				= getRefObject(methodInvocation);
		
		for (Pair<Product, IProduct<?, ?>> pair : refObject)
		{
			if (pair.getValue() == null)
			{
				continue;
			}
			String classification = pair.getKey()
			                            .value();
			checkClassificationExists(classification);
			this.event.addProduct(pair.getValue(), classification, event.classificationName(), getISystem(ActivityMasterSystemName), getISystemToken(ActivityMasterSystemName));
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
			classificationService.create(classificationName, classificationName, EventXProduct, getISystem(ActivityMasterSystemName), 0, "LogItemTypes", getISystemToken(ActivityMasterSystemName));
		}
	}
	
	private List<Pair<Product, IProduct<?, ?>>> getRefObject(MethodInvocation methodInvocation)
	{
		List<Pair<Product, IProduct<?, ?>>> output = new ArrayList<>();
		
		
		Parameter[] parameters = methodInvocation.getMethod()
		                                         .getParameters();
		for (int i = 0; i < parameters.length; i++)
		{
			Parameter parameter = parameters[i];
			if (parameter.isAnnotationPresent(Product.class))
			{
				output.add(Pair.of(parameter.getAnnotation(Product.class), (IProduct<?, ?>) methodInvocation.getArguments()[i]));
			}
		}
		if (output.isEmpty())
		{
			throw new ActivityMasterException("Unable to find any parameter with @Product to use for listener");
		}
		return output;
	}
}
