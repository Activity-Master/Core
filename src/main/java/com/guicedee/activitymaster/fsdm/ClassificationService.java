package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ClassificationException;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationQueryBuilder;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.SecurityTokenClassifications.*;


public class ClassificationService
		implements IClassificationService<ClassificationService>
{
	@Inject
	private IEnterprise<?,?> enterprise;
	
	@Inject
	private ClassificationsDataConceptService dataConceptService;
	
	public IClassification<?,?> get()
	{
		return new Classification();
	}
	
	@Override
	public IClassification<?,?> create(String name, String description, EnterpriseClassificationDataConcepts concept,
	                                 ISystems<?,?> system, Integer sequenceOrder, String parentName, java.util.UUID... identityToken)
	{
		IClassification<?,?> classification = find(parentName, system, identityToken);
		return create(name, description, concept, system, sequenceOrder, classification, identityToken);
	}
	
	@Override
	public IClassification<?,?> create(String name,
	                                 ISystems<?,?> system, java.util.UUID... identityToken)
	{
		return create(name, name, null, system, 0, identityToken);
	}
	
	@Override
	public IClassification<?,?> create(String name, String description,
	                                 ISystems<?,?> system, java.util.UUID... identityToken)
	{
		return create(name, description, null, system, 0, identityToken);
	}
	
	@Override
	public IClassification<?,?> create(String name, String description, EnterpriseClassificationDataConcepts conceptName,
	                                 ISystems<?,?> system, java.util.UUID... identityToken)
	{
		return create(name, description, conceptName, system, 0, identityToken);
	}
	
	@Override
	public IClassification<?,?> create(String name, String description, EnterpriseClassificationDataConcepts conceptName,
	                                 ISystems<?,?> system,
	                                 Integer sequenceNumber, java.util.UUID... identityToken)
	{
		return create(name, description, conceptName, system, sequenceNumber, (IClassification<?,?>) null, identityToken);
	}
	
	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IClassification<?,?> create(String name, String description, EnterpriseClassificationDataConcepts conceptName,
	                                 ISystems<?,?> system,
	                                 Integer sequenceNumber, IClassification<?,?> parent, java.util.UUID... identityToken)
	{
		ClassificationDataConcept dataConcept;
		if (conceptName != null)
		{
			dataConcept = dataConceptService.find(conceptName, system, identityToken);
		}
		else
		{
			dataConcept = dataConceptService.find("NoClassification", system, identityToken);
		}
		
		Classification rootCl = new Classification();
		
		boolean exists = rootCl.builder()
		                       .withName(name)
		                       .withEnterprise(enterprise)
		                       .inActiveRange()
		                       .inDateRange()
		                       .getCount() > 0;
		
		if (!exists)
		{
			rootCl.setName(name);
			rootCl.setDescription(description);
			rootCl.setClassificationSequenceNumber(sequenceNumber == null ? 1 : sequenceNumber);
			rootCl.setSystemID(system);
			rootCl.setOriginalSourceSystemID(system);
			rootCl.setOriginalSourceSystemUniqueID("");
			rootCl.setEnterpriseID(enterprise);
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?,?> activeFlag = acService.getActiveFlag(enterprise);
			rootCl.setActiveFlagID(activeFlag);
			rootCl.setConcept(dataConcept);
			rootCl.persistNow();
			if(!rootCl.builder().isRunDetached())
			rootCl.builder().getEntityManager().flush();
			
				rootCl.createDefaultSecurity(system, identityToken);
				
			if (parent != null && !NoClassification.toString().equals(name))
			{
				parent = (Classification) find(parent.getName(), system, identityToken);
				rootCl = (Classification) find(name, system, identityToken);
				@SuppressWarnings("unchecked")
				IClassification<Classification, ClassificationQueryBuilder> pp = (IClassification<Classification, ClassificationQueryBuilder>) parent;
				pp.addChild(rootCl,NoClassification.toString(),null, system, identityToken);
			}
		}
		else
		{
			return find(name, conceptName, system, identityToken);
		}
		return rootCl;
	}
	
	@CacheResult(cacheName = "ClassificationFindWithSimpleString")
	@Override
	public IClassification<?,?> find(@CacheKey String name, @CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		return find(name, null, system, identityToken);
	}
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "ClassificationFindWithSimpleStringWithConceptValue")
	@Override
	public IClassification<?,?> find(@CacheKey String name, @CacheKey EnterpriseClassificationDataConcepts concept, @CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		Classification search = new Classification();
		search = search.builder()
		               .withName(name)
		               .inActiveRange()
		               .inDateRange()
		            //   .canRead(system, identityToken)
		               .withEnterprise(enterprise)
		               .get()
		               .orElseThrow(()-> new ClassificationException("Cannot find Classification with name - [" + name + "] - and concept - [" + concept + "]"));
		return search;
	}
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "GetHierarchyTypeClassification")
	@Override
	public IClassification<?,?> getHierarchyType(@CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		return find(HierarchyTypeClassification.toString(),
				system,
				identityToken);
	}
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "GetNoClassification")
	@Override
	public IClassification<?,?> getNoClassification(@CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		return find(NoClassification.toString(),
				system,
				identityToken);
	}
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "IdentityTypeClassification")
	@Override
	public IClassification<?,?> getIdentityType(@CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		return find(Identity.name(),
				system,
				identityToken);
	}
}
