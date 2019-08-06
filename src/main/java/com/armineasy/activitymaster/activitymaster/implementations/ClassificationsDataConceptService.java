package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConcept;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.dto.IActiveFlag;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationDataConceptValue;
import com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.armineasy.activitymaster.activitymaster.services.system.IClassificationDataConceptService;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.google.inject.Singleton;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.Optional;
import java.util.UUID;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts.*;

@Singleton
public class ClassificationsDataConceptService
		implements IClassificationDataConceptService
{
	public ClassificationDataConcept createDataConcept(IClassificationDataConceptValue<?> name,
	                                                   String description,
	                                                   ISystems<?> system, UUID... identityToken)
	{
		ClassificationDataConcept newConcept = new ClassificationDataConcept();
		Optional<ClassificationDataConcept> exists =ActivityMasterConfiguration
				                                            .get()
				                                            .isDoubleCheckDisabled() ? Optional.empty() :
		                                            newConcept.builder()
		                                                       .findByName(name.classificationValue())
		                                                       .get();
		IActiveFlag<?> active = GuiceContext.get(IActiveFlagService.class)
		                                 .getActiveFlag(system.getEnterpriseID());

		if (exists.isEmpty())
		{
			newConcept.setDescription(description);
			newConcept.setName(name.classificationValue());
			newConcept.setSystemID((com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems) system);
			newConcept.setOriginalSourceSystemID((com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems) system);
			newConcept.setActiveFlagID((ActiveFlag)active);
			newConcept.setEnterpriseID((com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise) system.getEnterpriseID());
			newConcept.persist();
			if(GuiceContext.get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				newConcept.createDefaultSecurity(GuiceContext.get(ISystemsService.class).getActivityMaster(newConcept.getEnterpriseID(), identityToken),identityToken);
			}
		}
		else
		{
			newConcept = exists.get();
		}

		return newConcept;
	}

	@Override
	@CacheResult(cacheName = "GetGlobalConcept")
	public ClassificationDataConcept getGlobalConcept(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		ClassificationDataConcept globalClassifications = find(GlobalClassificationsDataConceptName, enterprise, identityToken);
		return globalClassifications;
	}

	@Override
	@CacheResult(cacheName = "FindConceptWithConceptValueAndSystem")
	public ClassificationDataConcept find(@CacheKey IClassificationDataConceptValue<?> name, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		ClassificationDataConcept cdc = new ClassificationDataConcept();
		cdc = cdc.builder()
		         .findByName(name.classificationValue())
		         .inActiveRange(enterprise,identityToken)
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
		ClassificationDataConcept globalClassifications = find(NoClassificationDataConceptName, enterprise, identityToken);
		return globalClassifications;
	}

	@Override
	@CacheResult(cacheName = "SecurityHierarchyConcept")
	public ClassificationDataConcept getSecurityHierarchyConcept(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		return find(EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, enterprise, identityToken);
	}

}
