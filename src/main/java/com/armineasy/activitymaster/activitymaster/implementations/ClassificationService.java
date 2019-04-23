package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConcept;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.system.IClassificationService;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.google.inject.Singleton;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.Optional;
import java.util.UUID;

import static com.armineasy.activitymaster.activitymaster.services.classifications.classification.Classifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.SecurityTokenClassifications.*;

@Singleton
public class ClassificationService
		implements IClassificationService
{
	public Classification create(IClassificationValue<?> concept,
	                             Systems system,IClassificationValue<?> parent,
	                             UUID...identityToken)
	{
		return create(concept, system, (short) 0, parent,identityToken);
	}

	public Classification create(IClassificationValue<?> concept,
	                             Systems system,
	                             UUID...identityToken)
	{
		return create(concept, system, (short) 0, null,identityToken);
	}

	public Classification create(IClassificationValue<?> concept,
	                             Systems system,
	                             Short sequenceNumber, UUID...identityToken)
	{
		return create(concept, system, sequenceNumber, null, identityToken);
	}

	public Classification create(IClassificationValue<?> concept,
	                             Systems system,
	                             Short sequenceNumber, IClassificationValue<?> parent, UUID...identityToken)
	{
		ClassificationsDataConceptService dataConceptService = GuiceContext.get(ClassificationsDataConceptService.class);
		ClassificationDataConcept dataConcept = dataConceptService.findConcept(concept.concept(), system.getEnterpriseID(), identityToken);
		Classification rootCl = new Classification();

		Optional<Classification> exists = rootCl.builder()
		                                        .findByNameAndConcept(concept.classificationName(), dataConcept, system.getEnterpriseID())
		                                        .withEnterprise(system.getEnterpriseID())
		                                        .inActiveRange(system.getEnterpriseID())
		                                        .inDateRange()
		                                        .get();
		if (exists.isEmpty())
		{
			rootCl.setName(concept.classificationName());
			rootCl.setDescription(concept.classificationDescription());
			rootCl.setClassificationSequenceNumber(sequenceNumber == null ? Short.valueOf("1") : sequenceNumber);
			rootCl.setSystemID(system);
			rootCl.setOriginalSourceSystemID(system);
			rootCl.setOriginalSourceSystemUniqueID("");
			rootCl.setEnterpriseID(system.getEnterpriseID());
			rootCl.setActiveFlagID(system.getActiveFlagID());
			rootCl.setConcept(dataConcept);
			rootCl.persist();
			if(GuiceContext.get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				rootCl.createDefaultSecurity(GuiceContext.get(ISystemsService.class).getActivityMaster(rootCl.getEnterpriseID(), identityToken),identityToken);
			}
		}
		else
		{
			rootCl = exists.get();
		}

		if(parent != null)
		{
			Classification parentClassification = find(parent, system.getEnterpriseID(), identityToken);
			parentClassification.addChild(rootCl, system.getEnterpriseID(), identityToken);
		}

		return rootCl;
	}

	@SuppressWarnings("Duplicates")
	@CacheResult(cacheName = "ClassificationFindWithIClassificationStringConceptValue")
	@Override
	public Classification find(@CacheKey IClassificationValue<?> name, @CacheKey Enterprise enterprise, @CacheKey UUID... identityToken)
	{
		Classification search = new Classification();
		ClassificationsDataConceptService cb = GuiceContext.get(ClassificationsDataConceptService.class);
		ClassificationDataConcept concept = cb.findConcept(name.concept(), enterprise,identityToken);
		search = search.builder()
		               .findByNameAndConcept(name.classificationName(), concept, enterprise)
		               .inActiveRange(enterprise,identityToken)
		               .inDateRange()
		               .canRead(enterprise, identityToken)
		               .get()
		               .get();
		return search;
	}
/*
	public ClassificationXClassification link(Classification parent, Classification child)
	{
		return link(parent, child, "");
	}

	public ClassificationXClassification link(Classification parent, Classification child, String value)
	{
		ClassificationXClassification link = new ClassificationXClassification();
		Optional<ClassificationXClassification> exists = new ClassificationXClassification().builder()
		                                                                                    .findLink(parent, child, parent.getEnterpriseID())
		                                                                                    .get();
		if (exists.isEmpty())
		{
			link.setParentClassificationID(parent);
			link.setChildClassificationID(child);
			link.setClassificationID(getHierarchyType(parent.getEnterpriseID()));
			link.setActiveFlagID(parent.getActiveFlagID());
			link.setEnterpriseID(parent.getEnterpriseID());
			link.setSystemID(parent.getSystemID());
			link.setOriginalSourceSystemID(parent.getSystemID());
			link.setValue(value);
			link.persist();
		}
		else
		{
			link = exists.get();
		}
		return link;
	}*/

	@CacheResult(cacheName = "GetHierarchyTypeClassification")
	@Override
	public Classification getHierarchyType(@CacheKey Enterprise enterprise, @CacheKey UUID... identityToken)
	{
		ClassificationService service = GuiceContext.get(ClassificationService.class);
		Classification hierarchyType = service.find(HierarchyTypeClassification,
		                                            enterprise,
		                                            identityToken);
		return hierarchyType;
	}

	@CacheResult(cacheName = "IdentityTypeClassification")
	@Override
	public Classification getIdentityType(@CacheKey Enterprise enterprise, @CacheKey UUID... identityToken)
	{
		ClassificationService service = GuiceContext.get(ClassificationService.class);
		Classification identityType = service.find(Identity,
		                                           enterprise,
		                                           identityToken);
		return identityType;
	}
}
