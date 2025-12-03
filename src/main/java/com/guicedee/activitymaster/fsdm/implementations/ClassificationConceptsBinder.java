package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.*;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationDataConceptService;
import com.guicedee.activitymaster.fsdm.ClassificationsDataConceptService;
import com.guicedee.client.services.lifecycle.IGuiceModule;

public class ClassificationConceptsBinder extends PrivateModule implements IGuiceModule<ClassificationConceptsBinder>
{
	@Override
	protected void configure()
	{
		@SuppressWarnings("Convert2Diamond")
		Key<IClassificationDataConceptService<?>> genericKey = Key.get(new TypeLiteral<IClassificationDataConceptService<?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<IClassificationDataConceptService<ClassificationsDataConceptService>> realKey
				= Key.get(new TypeLiteral<IClassificationDataConceptService<ClassificationsDataConceptService>>() {});
		
		bind(genericKey).to(realKey);
		bind(realKey).to(ClassificationsDataConceptService.class);
		bind(IClassificationDataConceptService.class).to(genericKey);
		
		expose(genericKey);
		expose(IClassificationDataConceptService.class);
	}
	
}
