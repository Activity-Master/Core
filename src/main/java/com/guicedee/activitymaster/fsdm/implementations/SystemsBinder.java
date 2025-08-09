package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.Key;
import com.google.inject.PrivateModule;
import com.google.inject.TypeLiteral;
import com.guicedee.activitymaster.fsdm.SystemsService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;
import lombok.extern.java.Log;

import java.util.HashSet;
import java.util.Set;

@Log
public class SystemsBinder extends PrivateModule implements IGuiceModule<SystemsBinder>
{
	private static Set<String> loadedSystems = new HashSet<String>();

	@Override
	protected void configure()
	{
		@SuppressWarnings("Convert2Diamond")
		Key<ISystemsService<?>> genericKey = Key.get(new TypeLiteral<ISystemsService<?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<ISystemsService<SystemsService>> realKey
				= Key.get(new TypeLiteral<ISystemsService<SystemsService>>() {});
		
		bind(genericKey).to(realKey);
		bind(realKey).to(SystemsService.class);
		bind(ISystemsService.class).to(genericKey);
		
		expose(genericKey);
		expose(ISystemsService.class);
		
		/*ClassInfoList classesImplementing = IGuiceContext.instance()
		                                                 .getScanResult()
		                                                 .getClassesImplementing(IActivityMasterSystem.class);
		for (ClassInfo classInfo : classesImplementing)
		{
			if(classInfo.isAbstract() || classInfo.isStatic())
				continue;
			Class<IActivityMasterSystem> aClass = (Class<IActivityMasterSystem>) classInfo.loadClass();
			IActivityMasterSystem system = null;
			try
			{
				system = aClass.newInstance();
			}
			catch (InstantiationException e)
			{
				throw new RuntimeException(e);
			}
			catch (IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}

			if(!loadedSystems.contains(system.getSystemName()))
			{
				loadedSystems.add(system.getSystemName());
			}else {
				continue;
			}*/
/*
			@SuppressWarnings("Convert2Diamond")
			Key<ISystems<?, ?>> aSystemGenericKey = Key.get(new TypeLiteral<ISystems<?, ?>>() {}, Names.named(system.getSystemName()));
			@SuppressWarnings("Convert2Diamond")
			Key<ISystems<Systems, SystemsQueryBuilder>> aSystemRealKey = Key.get(new TypeLiteral<ISystems<Systems, SystemsQueryBuilder>>() {}, Names.named(system.getSystemName()));
			
			bind(aSystemGenericKey).to(aSystemRealKey);
			bind(aSystemRealKey).toProvider(new SystemsProvider(system.getSystemName()));
			expose(aSystemGenericKey);
			
			log.config("Bound System with Name - " + system.getSystemName());
			
			Key<UUID> aSystemTokenGenericKey = Key.get(UUID.class, Names.named(system.getSystemName()));
			bind(aSystemTokenGenericKey).toProvider(new SystemsTokenProvider(system.getSystemName()));
			expose(aSystemTokenGenericKey);*/
		//}
	}
}
