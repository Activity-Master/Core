package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNo;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.google.inject.Singleton;
import com.jwebmp.guicedinjection.GuiceContext;

import java.util.Optional;
import java.util.UUID;

@Singleton
public class YesNoService
{
	public YesNo create(String defaultName, String singleChar, String booleanValue, String onOffValue, String activeValue,
	                    String inOutValue, Systems system, UUID... identityToken)
	{
		YesNo yn = new YesNo();
		Optional<YesNo> exists = yn.builder()
		                           .findByName(defaultName)
		                           .get();
		if (exists.isEmpty())
		{
			ActiveFlag activeFlag = GuiceContext.get(IActiveFlagService.class)
			                                    .getActiveFlag(system.getEnterpriseID());

			yn.setYesNoDesc(defaultName);
			yn.setYNDesc(singleChar);
			yn.setBooleanDesc(booleanValue);
			yn.setOnOffDesc(onOffValue);
			yn.setActiveDesc(activeValue);
			yn.setInOutDesc(inOutValue);
			yn.setEnterpriseID(system.getEnterpriseID());
			yn.setSystemID(system);
			yn.setOriginalSourceSystemID(system);
			yn.setActiveFlagID(activeFlag);
			yn.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				yn.createDefaultSecurity(GuiceContext.get(ISystemsService.class)
				                                     .getActivityMaster(yn.getEnterpriseID(), identityToken));
			}
		}
		else
		{
			yn = exists.get();
		}
		return yn;
	}

	public YesNo getYes(Enterprise enterprise)
	{
		return new YesNo().builder()
		                  .findYes()
		                  .withEnterprise(enterprise)
		                  .get()
		                  .get();
	}

	public YesNo getNo(Enterprise enterprise)
	{
		return new YesNo().builder()
		                  .findNo()
		                  .withEnterprise(enterprise)
		                  .inDateRange()
		                  .inVisibleRange(enterprise)
		                  .get()
		                  .get();
	}
}
