package com.guicedee.activitymaster.core.injections.updates;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.classifications.involvedparty.InvolvedPartyClassifications;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.activitymaster.core.updates.DatedUpdate;
import com.guicedee.activitymaster.core.updates.ISystemUpdate;

import static com.guicedee.activitymaster.core.SystemsService.*;
import static com.guicedee.activitymaster.core.services.classifications.involvedparty.InvolvedPartyClassifications.*;
import static com.guicedee.activitymaster.core.services.classifications.resourceitems.ResourceItemClassifications.*;

@DatedUpdate(date = "2016/01/01", taskCount = 3)
public class ClassificationBaseSetup implements ISystemUpdate
{
	@Inject
	private IClassificationService<?> service;
	
	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?> activityMasterSystem;
	
	@Override
	public void update(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Classifications System", "Loading Base Languages...", 1, progressMonitor);
		service.create(Languages, activityMasterSystem, Classifications.DefaultClassification);
		service.create(InvolvedPartyClassifications.ISO639_1, activityMasterSystem, Languages);
		service.create(InvolvedPartyClassifications.ISO639_2, activityMasterSystem, Languages);
		service.create(ISO6392EnglishName, activityMasterSystem, Languages);
		service.create(ISO6392FrenchName, activityMasterSystem, Languages);
		service.create(ISO6392GermanName, activityMasterSystem, Languages);
		
		logProgress("Classifications System", "Loading Default Devices...", 1, progressMonitor);
		service.create(Hardware, activityMasterSystem);
		service.create(Scanner, activityMasterSystem,Hardware);
		service.create(Printer, activityMasterSystem,Hardware);
		service.create(Phone, activityMasterSystem,Hardware);
		service.create(Computer, activityMasterSystem,Hardware);
		service.create(Desktop, activityMasterSystem,Computer);
		service.create(Laptop, activityMasterSystem,Computer);
	}
	
}
