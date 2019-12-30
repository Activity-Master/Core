package com.guicedee.activitymaster.core.implementations;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.google.inject.Singleton;
import com.guicedee.guicedinjection.GuiceContext;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.Optional;
import java.util.UUID;

import static com.guicedee.activitymaster.core.services.classifications.classification.Classifications.*;
import static com.guicedee.activitymaster.core.services.classifications.securitytokens.SecurityTokenClassifications.*;

@Singleton
public class ClassificationService
		implements IClassificationService<ClassificationService>
{
	@Override
	public IClassification<?> create(IClassificationValue<?> concept,
	                                 ISystems<?> system, IClassificationValue<?> parent,
	                                 UUID... identityToken)
	{
		return create(concept, system, (short) 0, parent, identityToken);
	}

	@Override
	public IClassification<?> create(IClassificationValue<?> concept,
	                                 ISystems<?> system,
	                                 UUID... identityToken)
	{
		return create(concept, system, (short) 0, null, identityToken);
	}

	@Override
	public IClassification<?> create(IClassificationValue<?> concept,
	                                 ISystems<?> system,
	                                 Short sequenceNumber, UUID... identityToken)
	{
		return create(concept, system, sequenceNumber, null, identityToken);
	}

	@Override
	public IClassification<?> create(IClassificationValue<?> concept,
	                                 ISystems<?> system,
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

	@Override
	public IClassification<?> create(String name, String description, IClassificationDataConceptValue<?> conceptName,
	                                 ISystems<?> system,
	                                 Short sequenceNumber, IClassificationValue<?> parent, UUID... identityToken)
	{
		ClassificationsDataConceptService dataConceptService = GuiceContext.get(ClassificationsDataConceptService.class);
		ClassificationDataConcept dataConcept = dataConceptService.find(conceptName, system.getEnterpriseID(), identityToken);
		Classification rootCl = new Classification();

		Optional<Classification> exists = ActivityMasterConfiguration
				                                  .get()
				                                  .isDoubleCheckDisabled() ? Optional.empty() :
		                                  rootCl.builder()
		                                        .findByNameAndConcept(name, dataConcept, system.getEnterpriseID())
		                                        .withEnterprise(system.getEnterpriseID())
		                                        .inActiveRange(system.getEnterpriseID())
		                                        .inDateRange()
		                                        .get();
		if (exists.isEmpty())
		{
			rootCl.setName(name);
			rootCl.setDescription(description);
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
		ClassificationDataConcept concept = cb.find(name.concept(), enterprise, identityToken);
		search = search.builder()
		               .findByNameAndConcept(name.classificationName(), concept, enterprise)
		               .inActiveRange(enterprise, identityToken)
		               .inDateRange()
		               .canRead(enterprise, identityToken)
		               .withEnterprise(enterprise)
		               .get()
		               .orElseThrow();
		return search;
	}

	@CacheResult(cacheName = "ClassificationFindWithSimpleString")
	@Override
	public IClassification<?> find(@CacheKey String name, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		Classification search = new Classification();
		ClassificationsDataConceptService cb = GuiceContext.get(ClassificationsDataConceptService.class);
		search = search.builder()
		               .findByName(name)
		               .inActiveRange(enterprise, identityToken)
		               .inDateRange()
		               .canRead(enterprise, identityToken)
		               .withEnterprise(enterprise)
		               .get()
		               .orElseThrow();
		return search;
	}

	@CacheResult(cacheName = "GetHierarchyTypeClassification")
	@Override
	public IClassification<?> getHierarchyType(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		ClassificationService service = GuiceContext.get(ClassificationService.class);
		return service.find(HierarchyTypeClassification,
		                    enterprise,
		                    identityToken);
	}

	@CacheResult(cacheName = "IdentityTypeClassification")
	@Override
	public IClassification<?> getIdentityType(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		ClassificationService service = GuiceContext.get(ClassificationService.class);
		return service.find(Identity,
		                    enterprise,
		                    identityToken);
	}
}
