package com.guicedee.activitymaster.core;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.client.services.IClassificationDataConceptService;
import com.guicedee.activitymaster.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConcept;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;

import java.util.NoSuchElementException;
import java.util.UUID;

import static com.guicedee.activitymaster.client.services.classifications.EnterpriseClassificationDataConcepts.*;


public class ClassificationsDataConceptService
		implements IClassificationDataConceptService<ClassificationsDataConceptService>
{
	@Inject
	@Named("Active")
	private IActiveFlag<?,?> activeFlag;
	
	@Inject
	private IEnterprise<?,?> enterprise;
	
	public ClassificationDataConcept createDataConcept(EnterpriseClassificationDataConcepts name,
	                                                   String description,
	                                                   ISystems<?,?> system,
	                                                   UUID... identityToken)
	{
		ClassificationDataConcept newConcept = new ClassificationDataConcept();
		boolean exists = newConcept.builder()
		                           .withName(name.classificationValue())
		                           .withEnterprise(enterprise)
		                           .inActiveRange(enterprise, identityToken)
		                           .inDateRange()
		                           .getCount() > 0;
		if (!exists)
		{
			newConcept.setDescription(description);
			newConcept.setName(name.classificationValue());
			newConcept.setSystemID(system);
			newConcept.setOriginalSourceSystemID(system);
			newConcept.setActiveFlagID(activeFlag);
			newConcept.setEnterpriseID(enterprise);
			newConcept.persist();
				newConcept.createDefaultSecurity(system, identityToken);
			
		}
		else
		{
			return find(name, system, identityToken);
		}
		
		return newConcept;
	}
	
	@Override
	@CacheResult(cacheName = "GetGlobalConcept")
	public ClassificationDataConcept getGlobalConcept(@CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		return find(GlobalClassificationsDataConceptName, system, identityToken);
	}
	
	@Override
	@CacheResult(cacheName = "FindConceptWithConceptValueAndSystem")
	public ClassificationDataConcept find(@CacheKey EnterpriseClassificationDataConcepts name, @CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		return find(name.classificationValue(), system, identityToken);
	}

	@CacheResult(cacheName = "FindConceptWithConceptValueAndSystemString")
	public ClassificationDataConcept find(@CacheKey String name, @CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		ClassificationDataConcept cdc = new ClassificationDataConcept();
		cdc = cdc.builder()
		         .withName(name)
		         .withEnterprise(enterprise)
		         .inActiveRange(enterprise, identityToken)
		         .inDateRange()
		       //  .canRead(system, identityToken)
		         .get()
		         .orElseThrow(()-> new NoSuchElementException("Cannot find Classification Data Concept with name - " + name));
		return cdc;
	}
	
	@Override
	@CacheResult(cacheName = "NoDataConcept")
	public ClassificationDataConcept getNoConcept(@CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		return find(NoClassificationDataConceptName, system, identityToken);
	}
	
	@Override
	@CacheResult(cacheName = "SecurityHierarchyConcept")
	public ClassificationDataConcept getSecurityHierarchyConcept(@CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		return find(EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, identityToken);
	}
	
}
