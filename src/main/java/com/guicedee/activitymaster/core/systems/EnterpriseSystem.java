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
