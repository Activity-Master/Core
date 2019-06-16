package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConcept;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.dto.IClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
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
		implements IClassificationService<ClassificationService>
{
	public IClassification<?> create(IClassificationValue<?> concept,
	                                 ISystems system, IClassificationValue<?> parent,
	                                 UUID... identityToken)
	{
		return create(concept, system, (short) 0, parent, identityToken);
	}

	public IClassification<?> create(IClassificationValue<?> concept,
	                                 ISystems system,
	                                 UUID... identityToken)
	{
		return create(concept, system, (short) 0, null, identityToken);
	}

	public IClassification<?> create(IClassificationValue<?> concept,
	                                 ISystems system,
	                                 Short sequenceNumber, UUID... identityToken)
	{
		return create(concept, system, sequenceNumber, null, identityToken);
	}

	public IClassification<?> create(IClassificationValue<?> concept,
	                                 ISystems system,
	                                 Short sequenceNumber, IClassificationValue<?> parent, UUID... identityToken)
	{
		ClassificationsDataConceptService dataConceptService = GuiceContext.get(ClassificationsDataConceptService.class);
		ClassificationDataConcept dataConcept = dataConceptService.find(concept.concept(), system.getEnterpriseID(), identityToken);
		Classification rootCl = new Classification();

		Optional<Classification> exists = ActivityMasterConfiguration
				                                  .get()
				                                  .isDoubleCheckDisabled() ? Optional.empty() :
		                                  rootCl.builder()
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
			rootCl.setSystemID((Systems) system);
			rootCl.setOriginalSourceSystemID((Systems) system);
			rootCl.setOriginalSourceSystemUniqueID("");
			rootCl.setEnterpriseID((Enterprise) system.getEnterpriseID());
			rootCl.setActiveFlagID(((Systems) system).getActiveFlagID());
			rootCl.setConcept(dataConcept);
			rootCl.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				rootCl.createDefaultSecurity(GuiceContext.get(ISystemsService.class)
				                                         .getActivityMaster(rootCl.getEnterpriseID(), identityToken), identityToken);
			}
		}
		else
		{
			rootCl = exists.get();
		}

		if (parent != null)
		{
			Classification parentClassification = (Classification) find(parent, system.getEnterpriseID(), identityToken);
			parentClassification.addChild(rootCl, system.getEnterpriseID(), identityToken);
		}

		return rootCl;
	}

	@SuppressWarnings("Duplicates")
	@CacheResult(cacheName = "ClassificationFindWithIClassificationStringConceptValue")
	@Override
	public IClassification<?> find(@CacheKey IClassificationValue<?> name, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		Classification search = new Classification();
		ClassificationsDataConceptService cb = GuiceContext.get(ClassificationsDataConceptService.class);
		ClassificationDataConcept concept = cb.find(name.concept(), (Enterprise) enterprise, identityToken);
		search = search.builder()
		               .findByNameAndConcept(name.classificationName(), concept, (Enterprise) enterprise)
		               .inActiveRange((Enterprise) enterprise, identityToken)
		               .inDateRange()
		               .canRead((Enterprise) enterprise, identityToken)
		               .get()
		               .get();
		return search;
	}

	@CacheResult(cacheName = "GetHierarchyTypeClassification")
	@Override
	public IClassification<?> getHierarchyType(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		ClassificationService service = GuiceContext.get(ClassificationService.class);
		Classification hierarchyType = (Classification) service.find(HierarchyTypeClassification,
		                                                             enterprise,
		                                                             identityToken);
		return hierarchyType;
	}

	@CacheResult(cacheName = "IdentityTypeClassification")
	@Override
	public IClassification<?> getIdentityType(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		ClassificationService service = GuiceContext.get(ClassificationService.class);
		Classification identityType = (Classification) service.find(Identity,
		                                                            enterprise,
		                                                            identityToken);
		return identityType;
	}
}
