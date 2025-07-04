package com.guicedee.activitymaster.fsdm.injections.updates;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.InvolvedPartyClassifications;
import com.guicedee.activitymaster.fsdm.client.services.systems.*;
import io.vertx.core.Future;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.InvolvedPartyClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.ResourceItemClassifications.*;
import static com.guicedee.activitymaster.fsdm.SystemsService.*;

@SortedUpdate(sortOrder = -500, taskCount = 3)
public class ClassificationBaseSetup implements ISystemUpdate
{
	@Inject
	private IClassificationService<?> service;

	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> activityMasterSystem;

	@Override
	public Future<Boolean> update(IEnterprise<?,?> enterprise)
	{
		logProgress("Classifications System", "Loading Base Languages...", 1);
		service.create(Languages, activityMasterSystem, DefaultClassifications.DefaultClassification);
		service.create(InvolvedPartyClassifications.ISO639_1, activityMasterSystem, Languages);
		service.create(InvolvedPartyClassifications.ISO639_2, activityMasterSystem, Languages);
		service.create(ISO6392EnglishName, activityMasterSystem, Languages);
		service.create(ISO6392FrenchName, activityMasterSystem, Languages);
		service.create(ISO6392GermanName, activityMasterSystem, Languages);

		logProgress("Classifications System", "Loading Default Devices...", 1);
		service.create(Hardware, activityMasterSystem);
		service.create(Scanner, activityMasterSystem,Hardware);
		service.create(Printer, activityMasterSystem,Hardware);
		service.create(Phone, activityMasterSystem,Hardware);
		service.create(Computer, activityMasterSystem,Hardware);
		service.create(Desktop, activityMasterSystem,Computer);
		service.create(Laptop, activityMasterSystem,Computer);

		return Future.succeededFuture(true);
	}

}
