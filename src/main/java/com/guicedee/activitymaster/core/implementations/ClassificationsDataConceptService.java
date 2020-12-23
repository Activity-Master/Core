package com.guicedee.activitymaster.core.implementations;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.core.services.dto.IActiveFlag;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IClassificationDataConceptService;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import java.util.UUID;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.GlobalClassificationsDataConceptName;
import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;


public class ClassificationsDataConceptService
		implements IClassificationDataConceptService<ClassificationsDataConceptService>
{
	@Override
	public ClassificationDataConcept createDataConcept(IClassificationDataConceptValue<?> name,
	                                                   String description,
	                                                   ISystems<?> system,
	                                                   UUID... identityToken)
	{
		ClassificationDataConcept newConcept = new ClassificationDataConcept();
		boolean exists = newConcept.builder()
				.withName(name.classificationValue())
				.withEnterprise(system.getEnterprise())
				.inActiveRange(system.getEnterprise(), identityToken)
				.inDateRange()
				.getCount() > 0;

		IActiveFlag<?> active = GuiceContext.get(IActiveFlagService.class)
		                                    .getActiveFlag(system.getEnterprise());

		if (!exists)
		{
			newConcept.setDescription(description);
			newConcept.setName(name.classificationValue());
			newConcept.setSystemID((Systems) system);
			newConcept.setOriginalSourceSystemID((Systems) system);
			newConcept.setActiveFlagID((ActiveFlag) active);
			newConcept.setEnterpriseID((Enterprise) active.getEnterpriseID());
			newConcept.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				newConcept.createDefaultSecurity(GuiceContext.get(ISystemsService.class)
				                                             .getActivityMaster(newConcept.getEnterpriseID(), identityToken), identityToken);
			}
		}
		else
		{
			return find(name, system.getEnterprise(), identityToken);
		}

		return newConcept;
	}

	@Override
	@CacheResult(cacheName = "GetGlobalConcept")
	public ClassificationDataConcept getGlobalConcept(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		return find(GlobalClassificationsDataConceptName, enterprise, identityToken);
	}

	@Override
	@CacheResult(cacheName = "FindConceptWithConceptValueAndSystem")
	public ClassificationDataConcept find(@CacheKey IClassificationDataConceptValue<?> name, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		return find(name.classificationValue(), enterprise, identityToken);
	}
	
	@Override
	@CacheResult(cacheName = "FindConceptWithConceptValueAndSystemString")
	public ClassificationDataConcept find(@CacheKey String name, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		ClassificationDataConcept cdc = new ClassificationDataConcept();
		cdc = cdc.builder()
		         .withName(name)
		         .withEnterprise(enterprise)
		         .inActiveRange(enterprise, identityToken)
		         .inDateRange()
		         .canRead(enterprise, identityToken)
		         .get()
		         .get();
		return cdc;
	}
	
	@Override
	@CacheResult(cacheName = "NoDataConcept")
	public ClassificationDataConcept getNoConcept(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		return find(NoClassificationDataConceptName, enterprise, identityToken);
	}

	@Override
	@CacheResult(cacheName = "SecurityHierarchyConcept")
	public ClassificationDataConcept getSecurityHierarchyConcept(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		return find(EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, enterprise, identityToken);
	}

}
