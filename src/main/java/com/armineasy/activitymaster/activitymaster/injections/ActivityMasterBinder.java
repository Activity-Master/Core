package com.armineasy.activitymaster.activitymaster.injections;

import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.implementations.*;
import com.armineasy.activitymaster.activitymaster.services.system.*;
import com.jwebmp.guicedinjection.abstractions.GuiceInjectorModule;
import com.jwebmp.guicedinjection.interfaces.IGuiceDefaultBinder;

public class ActivityMasterBinder
		implements IGuiceDefaultBinder<ActivityMasterBinder, GuiceInjectorModule>
{
	@Override
	public void onBind(GuiceInjectorModule module)
	{
		module.bind(IActiveFlagService.class)
		      .to(ActiveFlagService.class);

		module.bind(ISystemsService.class)
		      .to(SystemsService.class);

		module.bind(IClassificationService.class)
		      .to(ClassificationService.class);

		module.bind(IClassificationDataConceptService.class)
		      .to(ClassificationsDataConceptService.class);

		module.bind(ISecurityTokenService.class)
		      .to(SecurityTokenService.class);

		module.bind(IEnterpriseService.class)
		      .to(EnterpriseService.class);

		module.bind(IResourceItemService.class)
		      .to(ResourceItemService.class);

		module.bind(IInvolvedPartyService.class)
		      .to(InvolvedPartyService.class);

		module.bind(IEventService.class)
		      .to(EventsService.class);
	}
}
