package com.guicedee.activitymaster.core.systems;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.classifications.enterprise.EnterpriseClassifications;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IRelationshipValue;
import com.guicedee.activitymaster.core.services.system.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.core.updates.DatedUpdate;
import com.guicedee.activitymaster.core.updates.ISystemUpdate;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.json.LocalDateDeserializer;
import io.github.classgraph.ClassInfo;
import lombok.extern.java.Log;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@Singleton
@Log
public class EnterpriseSystem
		extends ActivityMasterDefaultSystem<EnterpriseSystem>
		implements IActivityMasterSystem<EnterpriseSystem>
{
	@Override
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
	
	}
	
	@Override
	public int totalTasks()
	{
		return 0;
	}
	
	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE;
	}
	
	@Override
	public void postStartup(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		super.postStartup(enterprise, progressMonitor);
	}
	
	@Override
	public void loadUpdates(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems newSystem = (Systems) getSystem(enterprise);
		UUID securityToken = getSystemToken(enterprise);
		if (!enterprise.hasClassifications(EnterpriseClassifications.LastUpdateDate, newSystem, securityToken))
		{
			enterprise.addOrReuse(EnterpriseClassifications.LastUpdateDate, "2008/01/01", newSystem, securityToken);
		}
		IRelationshipValue<IEnterprise<?>, IClassification<?>, ?> val = enterprise.addOrReuse(EnterpriseClassifications.LastUpdateDate, "2008/01/01", newSystem, securityToken);
		final LocalDate[] lastUpdateDate = {new LocalDateDeserializer().convert(val.getValue())};
		Map<LocalDate, Class<? extends ISystemUpdate>> availableUpdates = getUpdates(lastUpdateDate[0]);
		availableUpdates.forEach((key, value) -> {
			ISystemUpdate o = GuiceContext.get(value);
			o.update(enterprise, progressMonitor);
			lastUpdateDate[0] = key;
		});
		enterprise.addOrUpdate(EnterpriseClassifications.LastUpdateDate, DateTimeFormatter.ofPattern("yyyy/MM/dd")
		                                                                                  .format(lastUpdateDate[0]), newSystem, securityToken);
	}
	
	public Map<LocalDate, Class<? extends ISystemUpdate>> getUpdates(LocalDate lastUpdateDate)
	{
		Map<LocalDate, Class<? extends ISystemUpdate>> availableUpdates = new TreeMap<>();
		for (ClassInfo classInfo : GuiceContext.instance()
		                                       .getScanResult()
		                                       .getClassesWithAnnotation(DatedUpdate.class.getCanonicalName()))
		{
			if (classInfo.isAbstract() || classInfo.isInterface())
			{
				continue;
			}
			
			@SuppressWarnings("unchecked")
			Class<? extends ISystemUpdate> clazz = (Class<? extends ISystemUpdate>) classInfo.loadClass();
			DatedUpdate du = clazz.getAnnotation(DatedUpdate.class);
			LocalDate updateDate = null;
			updateDate = new LocalDateDeserializer().convert(du.date());
			availableUpdates.put(updateDate, clazz);
		}
		Map<LocalDate, Class<? extends ISystemUpdate>> applicableUpdates = new TreeMap<>();
		availableUpdates.forEach((key, value) -> {
			if (key.isAfter(lastUpdateDate))
			{
				applicableUpdates.put(key, value);
			}
		});
		return applicableUpdates;
	}
	
	@Override
	public String getSystemName()
	{
		return "Enterprise System";
	}
	
	@Override
	public String getSystemDescription()
	{
		return "The system for handling enterprises";
	}
	
}
