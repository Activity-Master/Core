package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.*;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.ClassificationService;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;

public class ClassificationsBinder extends PrivateModule implements IGuiceModule<ClassificationsBinder>
{
	@Override
	protected void configure()
	{
		@SuppressWarnings("Convert2Diamond")
		Key<IClassificationService<?>> genericKey = Key.get(new TypeLiteral<IClassificationService<?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<IClassificationService<ClassificationService>> realKey
				= Key.get(new TypeLiteral<IClassificationService<ClassificationService>>() {});
		
		bind(genericKey).to(realKey);
		bind(realKey).to(ClassificationService.class);
		bind(IClassificationService.class).to(genericKey);
		
		expose(genericKey);
		expose(IClassificationService.class);
	}
	
}
