package com.guicedee.activitymaster.core;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.client.services.IClassificationService;
import com.guicedee.activitymaster.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.core.db.entities.classifications.builders.ClassificationQueryBuilder;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;

import java.util.NoSuchElementException;
import java.util.UUID;

import static com.guicedee.activitymaster.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.activitymaster.client.services.classifications.SecurityTokenClassifications.*;


public class ClassificationService
		implements IClassificationService<ClassificationService>
{
	@Inject
	@Named("Active")
	private IActiveFlag<?,?> activeFlag;
	
	@Inject
	private IEnterprise<?,?> enterprise;
	
	@Inject
	private ClassificationsDataConceptService dataConceptService;
	
	@Override
	public IClassification<?,?> create(String name, String description, EnterpriseClassificationDataConcepts concept,
	                                 ISystems<?,?> system, Integer sequenceOrder, String parentName, UUID... identityToken)
	{
		IClassification<?,?> classification = find(parentName, system, identityToken);
		return create(name, description, concept, system, sequenceOrder, classification, identityToken);
	}
	
	@Override
	public IClassification<?,?> create(String name,
	                                 ISystems<?,?> system, UUID... identityToken)
	{
		return create(name, name, null, system, 0, identityToken);
	}
	
	@Override
	public IClassification<?,?> create(String name, String description,
	                                 ISystems<?,?> system, UUID... identityToken)
	{
		return create(name, description, null, system, 0, identityToken);
	}
	
	@Override
	public IClassification<?,?> create(String name, String description, EnterpriseClassificationDataConcepts conceptName,
	                                 ISystems<?,?> system, UUID... identityToken)
	{
		return create(name, description, conceptName, system, 0, identityToken);
	}
	
	@Override
	public IClassification<?,?> create(String name, String description, EnterpriseClassificationDataConcepts conceptName,
	                                 ISystems<?,?> system,
	                                 Integer sequenceNumber, UUID... identityToken)
	{
		return create(name, description, conceptName, system, sequenceNumber, (IClassification<?,?>) null, identityToken);
	}
	
	@Override
	public IClassification<?,?> create(String name, String description, EnterpriseClassificationDataConcepts conceptName,
	                                 ISystems<?,?> system,
	                                 Integer sequenceNumber, IClassification<?,?> parent, UUID... identityToken)
	{
		ClassificationDataConcept dataConcept;
		if (conceptName != null)
		{
			dataConcept = dataConceptService.find(conceptName, system, identityToken);
		}
		else
		{
			dataConcept = dataConceptService.find("No Classification", system, identityToken);
		}
		
		Classification rootCl = new Classification();
		
		boolean exists = rootCl.builder()
		                       .findByNameAndConcept(name, dataConcept)
		                       .withEnterprise(enterprise)
		                       .inActiveRange(enterprise)
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
			rootCl.setActiveFlagID(activeFlag);
			rootCl.setConcept(dataConcept);
			rootCl.persist();
				rootCl.createDefaultSecurity(system, identityToken);
			
			
			if (parent != null)
			{
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
	public IClassification<?,?> find(@CacheKey String name, @CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		return find(name, null, system, identityToken);
	}

	@CacheResult(cacheName = "ClassificationFindWithSimpleStringWithConceptValue")
	@Override
	public IClassification<?,?> find(@CacheKey String name, @CacheKey EnterpriseClassificationDataConcepts concept, @CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		Classification search = new Classification();
		search = search.builder()
		               .findByNameAndConcept(name, concept, system, identityToken)
		               .inActiveRange(enterprise, identityToken)
		               .inDateRange()
		            //   .canRead(system, identityToken)
		               .withEnterprise(enterprise)
		               .get()
		               .orElseThrow(()-> new NoSuchElementException("Cannot find Classification with name - [" + name + "] - and concept - [" + concept + "]"));
		return search;
	}
	
	@CacheResult(cacheName = "GetHierarchyTypeClassification")
	@Override
	public IClassification<?,?> getHierarchyType(@CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		return find(HierarchyTypeClassification.toString(),
				system,
				identityToken);
	}
	
	@CacheResult(cacheName = "GetNoClassification")
	@Override
	public IClassification<?,?> getNoClassification(@CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		return find(NoClassification.toString(),
				system,
				identityToken);
	}
	
	@CacheResult(cacheName = "IdentityTypeClassification")
	@Override
	public IClassification<?,?> getIdentityType(@CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		return find(Identity.name(),
				system,
				identityToken);
	}
}
