package com.guicedee.activitymaster.core;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;

import java.util.NoSuchElementException;
import java.util.UUID;

import static com.guicedee.activitymaster.core.services.classifications.classification.Classifications.*;
import static com.guicedee.activitymaster.core.services.classifications.securitytokens.SecurityTokenClassifications.*;


public class ClassificationService
		implements IClassificationService<ClassificationService>
{
	@Inject
	@Named("Active")
	private IActiveFlag<?> activeFlag;
	
	@Inject
	private IEnterprise<?> enterprise;
	
	@Inject
	private ClassificationsDataConceptService dataConceptService;
	
	@Override
	public IClassification<?> create(IClassificationValue<?> name,
	                                 ISystems<?> system, UUID... identityToken)
	{
		return create(name.classificationName(), name.classificationDescription(),
				name.concept()
				    .classificationValue(), system, 0, identityToken);
	}
	
	@Override
	public IClassification<?> create(IClassificationValue<?> name,
	                                 ISystems<?> system, IClassificationValue<?> parentName, UUID... identityToken)
	{
		IClassification<?> classification = find(parentName, system, identityToken);
		return create(name.classificationName(), name.classificationDescription(),
				name.concept()
				    .classificationValue(), system, 0, classification, identityToken);
	}
	
	@Override
	public IClassification<?> create(String name, String description, String concept,
	                                 ISystems<?> system, Integer sequenceOrder, String parentName, UUID... identityToken)
	{
		IClassification<?> classification = find(parentName, system, identityToken);
		return create(name, description, concept, system, sequenceOrder, classification, identityToken);
	}
	
	@Override
	public IClassification<?> create(IClassificationValue<?> name,
	                                 ISystems<?> system, Integer sequenceOrder, IClassificationValue<?> parentName, UUID... identityToken)
	{
		IClassification<?> classification = find(parentName, system, identityToken);
		return create(name.classificationName(), name.classificationDescription(),
				name.concept()
				    .classificationValue(), system, sequenceOrder, classification, identityToken);
	}
	
	@Override
	public IClassification<?> create(IClassificationValue<?> name,
	                                 ISystems<?> system, String parentName, UUID... identityToken)
	{
		IClassification<?> classification = find(parentName, system, identityToken);
		return create(name.classificationName(), name.classificationDescription(),
				name.concept()
				    .classificationValue(), system, 0, classification, identityToken);
	}
	
	@Override
	public IClassification<?> create(IClassificationValue<?> name,
	                                 ISystems<?> system, Integer sequenceNumber, UUID... identityToken)
	{
		return create(name.classificationName(), name.classificationDescription(),
				name.concept()
				    .classificationValue(), system, sequenceNumber, (IClassification<?>) null, identityToken);
	}
	
	
	@Override
	public IClassification<?> create(String name,
	                                 ISystems<?> system, UUID... identityToken)
	{
		return create(name, name, null, system, 0, identityToken);
	}
	
	@Override
	public IClassification<?> create(String name, String description,
	                                 ISystems<?> system, UUID... identityToken)
	{
		return create(name, description, null, system, 0, identityToken);
	}
	
	@Override
	public IClassification<?> create(String name, String description, String conceptName,
	                                 ISystems<?> system, UUID... identityToken)
	{
		return create(name, description, conceptName, system, 0, identityToken);
	}
	
	@Override
	public IClassification<?> create(String classificationName, String classificationDescription, String conceptName, ISystems<?> system, Integer sequenceNumber, IClassificationValue<?> parentClass, UUID... identityToken)
	{
		IClassification<?> classification = find(parentClass.classificationName(), system, identityToken);
		return create(classificationName, classificationDescription, conceptName, system, sequenceNumber, classification, identityToken);
	}
	
	@Override
	public IClassification<?> create(String name, String description, String conceptName,
	                                 ISystems<?> system,
	                                 Integer sequenceNumber, UUID... identityToken)
	{
		return create(name, description, conceptName, system, sequenceNumber, (IClassification<?>) null, identityToken);
	}
	
	@Override
	public IClassification<?> create(String name, String description, String conceptName,
	                                 ISystems<?> system,
	                                 Integer sequenceNumber, IClassification<?> parent, UUID... identityToken)
	{
		ClassificationDataConcept dataConcept;
		if (!Strings.isNullOrEmpty(conceptName))
		{
			dataConcept = dataConceptService.find(conceptName, system, identityToken);
		}
		else
		{
			dataConcept = dataConceptService.find("No Classification", system, identityToken);;
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
			rootCl.setSystemID((Systems) system);
			rootCl.setOriginalSourceSystemID((Systems) system);
			rootCl.setOriginalSourceSystemUniqueID("");
			rootCl.setEnterpriseID((Enterprise) enterprise);
			rootCl.setActiveFlagID(activeFlag);
			rootCl.setConcept(dataConcept);
			rootCl.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				rootCl.createDefaultSecurity(system, identityToken);
			}
			
			if (parent != null)
			{
				parent.addChild(rootCl, system, identityToken);
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
	public IClassification<?> find(@CacheKey String name, @CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		return find(name, (String) null, system, identityToken);
	}
	
	@CacheResult(cacheName = "ClassificationFindWithSimpleString")
	@Override
	public IClassification<?> find(@CacheKey IClassificationValue<?> name, @CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		return find(name.classificationName(), (String) null, system, identityToken);
	}
	
	@CacheResult(cacheName = "ClassificationFindWithSimpleStringWithConcept")
	@Override
	public IClassification<?> find(@CacheKey String name, @CacheKey IClassificationDataConcept<?> concept, @CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		return find(name, concept.getName(), system, identityToken);
	}
	
	
	@CacheResult(cacheName = "ClassificationFindWithSimpleStringWithConceptValue")
	@Override
	public IClassification<?> find(@CacheKey String name, @CacheKey String concept, @CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		Classification search = new Classification();
		search = search.builder()
		               .findByNameAndConcept(name, concept, system, identityToken)
		               .inActiveRange(enterprise, identityToken)
		               .inDateRange()
		               .canRead(system, identityToken)
		               .withEnterprise(enterprise)
		               .get()
		               .orElseThrow(()-> new NoSuchElementException("Cannot find Classification with name - [" + name + "] - and concept - [" + concept + "]"));
		return search;
	}
	
	@CacheResult(cacheName = "GetHierarchyTypeClassification")
	@Override
	public IClassification<?> getHierarchyType(@CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		return find(HierarchyTypeClassification.classificationName(),
				system,
				identityToken);
	}
	
	@CacheResult(cacheName = "GetNoClassification")
	@Override
	public IClassification<?> getNoClassification(@CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		return find(NoClassification.classificationName(),
				system,
				identityToken);
	}
	
	@CacheResult(cacheName = "IdentityTypeClassification")
	@Override
	public IClassification<?> getIdentityType(@CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		return find(Identity.classificationName(),
				system,
				identityToken);
	}
}
