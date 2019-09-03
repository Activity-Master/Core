package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNo;
import com.armineasy.activitymaster.activitymaster.services.dto.IActiveFlag;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
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
	                    String inOutValue, ISystems<?> system, UUID... identityToken)
	{
		YesNo yn = new YesNo();
		Optional<YesNo> exists = yn.builder()
		                           .withEnterprise(system.getEnterprise())
		                           .findByName(defaultName)
		                           .inDateRange()
		                           .inActiveRange(system.getEnterprise(),identityToken)
		                           .canRead(system.getEnterprise(),identityToken)
		                           .get();
		if (exists.isEmpty())
		{
			IActiveFlag<?> activeFlag = GuiceContext.get(IActiveFlagService.class)
			                                     .getActiveFlag(system.getEnterpriseID());

			yn.setYesNoDesc(defaultName);
			yn.setYNDesc(singleChar);
			yn.setBooleanDesc(booleanValue);
			yn.setOnOffDesc(onOffValue);
			yn.setActiveDesc(activeValue);
			yn.setInOutDesc(inOutValue);
			yn.setEnterpriseID((Enterprise) system.getEnterpriseID());
			yn.setSystemID((Systems) system);
			yn.setOriginalSourceSystemID((Systems) system);
			yn.setActiveFlagID((ActiveFlag)activeFlag);
			yn.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				yn.createDefaultSecurity(GuiceContext.get(ISystemsService.class)
				                                     .getActivityMaster(yn.getEnterpriseID(), identityToken), identityToken);
			}
		}
		else
		{
			yn = exists.get();
		}
		return yn;
	}

	public YesNo getYes(IEnterprise<?> enterprise)
	{
		return new YesNo().builder()
		                  .findYes()
		                  .withEnterprise(enterprise)
		                  .inDateRange()
		                  .inVisibleRange(enterprise)
		                  .get()
		                  .get();
	}

	public YesNo getNo(IEnterprise<?> enterprise)
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
