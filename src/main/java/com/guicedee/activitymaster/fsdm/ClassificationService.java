package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.types.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.client.types.exceptions.ClassificationException;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationQueryBuilder;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import jakarta.persistence.EntityManager;

import static com.guicedee.activitymaster.fsdm.client.types.classifications.DefaultClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.types.classifications.SecurityTokenClassifications.*;


public class ClassificationService
		implements IClassificationService<ClassificationService>
{
	@Inject
	private ClassificationsDataConceptService dataConceptService;
	
	@Inject
	@com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB
	private EntityManager entityManager;
	
	public IClassification<?, ?> get()
	{
		return new Classification();
	}
	
	@Override
	public IClassification<?, ?> create(String name, String description, EnterpriseClassificationDataConcepts concept,
	                                    ISystems<?, ?> system, Integer sequenceOrder, String parentName, java.util.UUID... identityToken)
	{
		IClassification<?, ?> classification = find(parentName, system, identityToken);
		return create(name, description, concept.classificationValue(), system, sequenceOrder, classification, identityToken);
	}
	
	@Override
	public IClassification<?, ?> create(String name,
	                                    ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return create(name, name, null, system, 0, identityToken);
	}
	
	@Override
	public IClassification<?, ?> create(String name, String description,
	                                    ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return create(name, description, null, system, 0, identityToken);
	}
	
	@Override
	public IClassification<?, ?> create(String name, String description, EnterpriseClassificationDataConcepts conceptName,
	                                    ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return create(name, description, conceptName, system, 0, identityToken);
	}
	
	@Override
	public IClassification<?, ?> create(String name, String description, EnterpriseClassificationDataConcepts conceptName,
	                                    ISystems<?, ?> system,
	                                    Integer sequenceNumber, java.util.UUID... identityToken)
	{
		return create(name, description, conceptName.classificationValue(), system, sequenceNumber, (IClassification<?, ?>) null, identityToken);
	}
	
	@Override
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IClassification<?, ?> create(String name, String description, String conceptName,
	                                    ISystems<?, ?> system,
	                                    Integer sequenceNumber, IClassification<?, ?> parent, java.util.UUID... identityToken)
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
		
		boolean exists = rootCl.builder(entityManager)
		                       .withName(name)
		                       .withEnterprise(system.getEnterprise())
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
			rootCl.setEnterpriseID(system.getEnterprise());
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(system.getEnterprise());
			rootCl.setActiveFlagID(activeFlag);
			rootCl.setConcept(dataConcept);
			rootCl.persistNow(entityManager);
			
			rootCl.createDefaultSecurity(system, identityToken);
			
			if (parent != null && !NoClassification.toString()
			                                       .equals(name))
			{
				parent = (Classification) find(parent.getName(), system, identityToken);
				rootCl = (Classification) find(name, system, identityToken);
				@SuppressWarnings("unchecked")
				IClassification<Classification, ClassificationQueryBuilder> pp = (IClassification<Classification, ClassificationQueryBuilder>) parent;
				pp.addChild(rootCl, NoClassification.toString(), null, system, identityToken);
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
	public IClassification<?, ?> find(@CacheKey String name, @CacheKey ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
	{
		return find(name, null, system, identityToken);
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "ClassificationFindWithSimpleStringWithConceptValue")
	@Override
	public IClassification<?, ?> find(@CacheKey String name, @CacheKey String concept, @CacheKey ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
	{
		Classification search = new Classification();
		search = search.builder(entityManager)
		               .withName(name)
		               .inActiveRange()
		               .inDateRange()
		               //   .canRead(system, identityToken)
		               .withEnterprise(system.getEnterprise())
		               .get()
		               .orElseThrow(() -> new ClassificationException("Cannot find Classification with name - [" + name + "] - and concept - [" + concept + "]"));
		return search;
	}
	
	@Override
	public boolean doesClassificationExist(@CacheKey String name, @CacheKey String concept, @CacheKey ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
	{
		Classification search = new Classification();
		return search.builder(entityManager)
		             .withName(name)
		             .inActiveRange()
		             .inDateRange()
		             //   .canRead(system, identityToken)
		             .withEnterprise(system.getEnterprise())
		             .getCount() > 0
				;
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "GetHierarchyTypeClassification")
	@Override
	public IClassification<?, ?> getHierarchyType(@CacheKey ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
	{
		return find(HierarchyTypeClassification.toString(),
				system,
				identityToken);
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "GetNoClassification")
	@Override
	public IClassification<?, ?> getNoClassification(@CacheKey ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
	{
		return find(NoClassification.toString(),
				system,
				identityToken);
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "IdentityTypeClassification")
	@Override
	public IClassification<?, ?> getIdentityType(@CacheKey ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
	{
		return find(Identity.name(),
				system,
				identityToken);
	}
}
