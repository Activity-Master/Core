package com.guicedee.activitymaster.core.injections;

import com.guicedee.activitymaster.core.implementations.*;
import com.guicedee.activitymaster.core.services.system.*;
import com.guicedee.activitymaster.core.systems.TimeSystem;
import com.guicedee.guicedinjection.abstractions.GuiceInjectorModule;
import com.guicedee.guicedinjection.interfaces.IGuiceDefaultBinder;

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

		module.bind(IAddressService.class)
		      .to(AddressService.class);

		module.bind(IArrangementsService.class)
		      .to(ArrangementsService.class);

		module.bind(ITimeSystem.class)
		      .to(TimeSystem.class);
	}
}
