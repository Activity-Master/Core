package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationDataConceptService;
import com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;

import java.util.NoSuchElementException;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.*;


public class ClassificationsDataConceptService
		implements IClassificationDataConceptService<ClassificationsDataConceptService>
{
	@Inject
	private IEnterprise<?,?> enterprise;
	
	@Override
	public IClassificationDataConcept<?,?> get()
	{
		return new ClassificationDataConcept();
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public ClassificationDataConcept createDataConcept(EnterpriseClassificationDataConcepts name,
	                                                   String description,
	                                                   ISystems<?,?> system,
	                                                   java.util.UUID... identityToken)
	{
		ClassificationDataConcept newConcept = new ClassificationDataConcept();
		boolean exists = newConcept.builder()
		                           .withName(name.classificationValue())
		                           .withEnterprise(enterprise)
		                           .inActiveRange()
		                           .inDateRange()
		                           .getCount() > 0;
		if (!exists)
		{
			newConcept.setDescription(description);
			newConcept.setName(name.classificationValue());
			newConcept.setSystemID(system);
			newConcept.setOriginalSourceSystemID(system);
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?,?> activeFlag = acService.getActiveFlag(enterprise);
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
	public ClassificationDataConcept getGlobalConcept(@CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		return find(GlobalClassificationsDataConceptName, system, identityToken);
	}
	
	@Override
	@CacheResult(cacheName = "FindConceptWithConceptValueAndSystem")
	public ClassificationDataConcept find(@CacheKey EnterpriseClassificationDataConcepts name, @CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		return find(name.classificationValue(), system, identityToken);
	}
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "FindConceptWithConceptValueAndSystemString")
	public ClassificationDataConcept find(@CacheKey String name, @CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		ClassificationDataConcept cdc = new ClassificationDataConcept();
		cdc = cdc.builder()
		         .withName(name)
		         .withEnterprise(enterprise)
		         .inActiveRange()
		         .inDateRange()
		       //  .canRead(system, identityToken)
		         .get()
		         .orElseThrow(()-> new NoSuchElementException("Cannot find Classification Data Concept with name - " + name));
		return cdc;
	}
	
	@Override
	@CacheResult(cacheName = "NoDataConcept")
	public ClassificationDataConcept getNoConcept(@CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		return find(NoClassificationDataConceptName, system, identityToken);
	}
	
	@Override
	@CacheResult(cacheName = "SecurityHierarchyConcept")
	public ClassificationDataConcept getSecurityHierarchyConcept(@CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		return find(EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, identityToken);
	}
	
}
