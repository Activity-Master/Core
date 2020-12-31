package com.guicedee.activitymaster.core.implementations;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IClassificationDataConcept;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import java.util.UUID;

import static com.guicedee.activitymaster.core.services.classifications.classification.Classifications.*;
import static com.guicedee.activitymaster.core.services.classifications.securitytokens.SecurityTokenClassifications.*;


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
	public IClassification<?> create(IClassificationValue<?> classificationValue,
	                                 ISystems<?> system,
	                                 Short sequenceNumber, IClassificationValue<?> parent, UUID... identityToken)
	{
		ClassificationsDataConceptService dataConceptService = GuiceContext.get(ClassificationsDataConceptService.class);
		ClassificationDataConcept dataConcept = dataConceptService.find(classificationValue.concept(), system.getEnterpriseID(), identityToken);
		Classification rootCl = new Classification();
		
		boolean exists = rootCl.builder()
		                       .findByNameAndConcept(classificationValue.classificationName(), dataConcept)
		                       .withEnterprise(system.getEnterpriseID())
		                       .inActiveRange(system.getEnterpriseID())
		                       .inDateRange()
		                       .getCount() > 0;
		
		if (!exists)
		{
			rootCl.setName(classificationValue.classificationName());
			rootCl.setDescription(classificationValue.classificationDescription());
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
			
			if (parent != null)
			{
				IClassification<?> parentClassification = find(parent, system.getEnterpriseID(), identityToken);
				parentClassification.addChild(rootCl, system.getEnterpriseID(), identityToken);
			}
		}
		else
		{
			return find(classificationValue, system.getEnterprise(), identityToken);
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
		
		boolean exists = rootCl.builder()
		                       .findByNameAndConcept(name, dataConcept)
		                       .withEnterprise(system.getEnterpriseID())
		                       .inActiveRange(system.getEnterpriseID())
		                       .inDateRange()
		                       .getCount() > 0;
		
		if (!exists)
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
			if (parent != null)
			{
				IClassification<?> parentClassification = find(parent.name(), system.getEnterpriseID(), identityToken);
				parentClassification.addChild(rootCl, system.getEnterpriseID(), identityToken);
			}
		}
		else
		{
			return find(name, conceptName, system.getEnterprise(), identityToken);
		}
		return rootCl;
	}
	
	@Override
	public IClassification<?> create(String name, String description, IClassificationDataConceptValue<?> conceptName,
	                                 ISystems<?> system,
	                                 Short sequenceNumber, String parent, UUID... identityToken)
	{
		ClassificationsDataConceptService dataConceptService = GuiceContext.get(ClassificationsDataConceptService.class);
		ClassificationDataConcept dataConcept = dataConceptService.find(conceptName, system.getEnterpriseID(), identityToken);
		Classification rootCl = new Classification();
		
		boolean exists = rootCl.builder()
		                       .findByNameAndConcept(name, dataConcept)
		                       .withEnterprise(system.getEnterpriseID())
		                       .inActiveRange(system.getEnterpriseID())
		                       .inDateRange()
		                       .getCount() > 0;
		
		if (!exists)
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
			if (parent != null)
			{
				Classification parentClassification = (Classification) find(parent, system.getEnterpriseID(), identityToken);
				parentClassification.addChild(rootCl, system.getEnterpriseID(), identityToken);
			}
		}
		else
		{
			return find(name, conceptName, system.getEnterprise(), identityToken);
		}
		
		return rootCl;
	}
	
	@Override
	public IClassification<?> create(String name, String description, IClassificationDataConceptValue<?> conceptName,
	                                 ISystems<?> system,
	                                 Short sequenceNumber, IClassification<?> parent, UUID... identityToken)
	{
		ClassificationsDataConceptService dataConceptService = GuiceContext.get(ClassificationsDataConceptService.class);
		ClassificationDataConcept dataConcept = dataConceptService.find(conceptName, system.getEnterpriseID(), identityToken);
		Classification rootCl = new Classification();
		
		boolean exists = rootCl.builder()
		                       .findByNameAndConcept(name, dataConcept)
		                       .withEnterprise(system.getEnterpriseID())
		                       .inActiveRange(system.getEnterpriseID())
		                       .inDateRange()
		                       .getCount() > 0;
		
		if (!exists)
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
			
			if (parent != null)
			{
				parent.addChild(rootCl, system.getEnterpriseID(), identityToken);
			}
		}
		else
		{
			return find(name, conceptName, system.getEnterprise(), identityToken);
		}
		return rootCl;
	}
	
	@Override
	public IClassification<?> create(String name, String description, IClassificationDataConceptValue<?> conceptName,
	                                 IEnterprise<?> enterprise,
	                                 Short sequenceNumber, IClassification<?> parent, UUID... identityToken)
	{
		ClassificationsDataConceptService dataConceptService = GuiceContext.get(ClassificationsDataConceptService.class);
		ClassificationDataConcept dataConcept = dataConceptService.find(conceptName, enterprise, identityToken);
		Classification rootCl = new Classification();
		
		boolean exists = rootCl.builder()
		                       .findByNameAndConcept(name, dataConcept)
		                       .withEnterprise(enterprise)
		                       .inActiveRange(enterprise, identityToken)
		                       .inDateRange()
		                       .getCount() > 0;
		
		if (!exists)
		{
			rootCl.setName(name);
			rootCl.setDescription(description);
			rootCl.setClassificationSequenceNumber(sequenceNumber == null ? Short.valueOf("1") : sequenceNumber);
			rootCl.setOriginalSourceSystemUniqueID("");
			rootCl.setEnterpriseID((Enterprise) enterprise);
			rootCl.setActiveFlagID(dataConcept.getActiveFlagID());
			rootCl.setConcept(dataConcept);
			rootCl.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				rootCl.createDefaultSecurity(GuiceContext.get(ISystemsService.class)
				                                         .getActivityMaster(rootCl.getEnterpriseID(), identityToken), identityToken);
			}
			
			if (parent != null)
			{
				parent.addChild(rootCl, enterprise, identityToken);
			}
		}
		else
		{
			return find(name, conceptName, enterprise, identityToken);
		}
		return rootCl;
	}
	
	@Override
	public IClassification<?> create(String name, String description, IClassificationDataConceptValue<?> conceptName,
	                                 ISystems<?> system,
	                                 Short sequenceNumber, UUID... identityToken)
	{
		ClassificationsDataConceptService dataConceptService = GuiceContext.get(ClassificationsDataConceptService.class);
		ClassificationDataConcept dataConcept = dataConceptService.find(conceptName, system.getEnterpriseID(), identityToken);
		Classification rootCl = new Classification();
		
		boolean exists = rootCl.builder()
		                       .findByNameAndConcept(name, dataConcept)
		                       .withEnterprise(system.getEnterpriseID())
		                       .inActiveRange(system.getEnterpriseID())
		                       .inDateRange()
		                       .getCount() > 0;
		
		if (!exists)
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
			return find(name, conceptName, system.getEnterprise(), identityToken);
		}
		return rootCl;
	}
	
	@Override
	public IClassification<?> find(@CacheKey IClassificationValue<?> name, @CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		return find(name, system.getEnterpriseID(), identityToken);
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
		               .findByNameAndConcept(name.classificationName(), concept)
		               .inActiveRange(enterprise, identityToken)
		               .inDateRange()
		               .canRead(enterprise, identityToken)
		               .withEnterprise(enterprise)
		               .get()
		               .orElseThrow();
		return search;
	}
	
	@Override
	public IClassification<?> find(@CacheKey String name, @CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		return find(name, system.getEnterpriseID(), identityToken);
	}
	
	@CacheResult(cacheName = "ClassificationFindWithSimpleString")
	@Override
	public IClassification<?> find(@CacheKey String name, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		Classification search = new Classification();
		ClassificationsDataConceptService cb = GuiceContext.get(ClassificationsDataConceptService.class);
		search = search.builder()
		               .withName(name)
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
	public IClassification<?> findOrCreate(@CacheKey String name, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		Classification search = new Classification();
		ClassificationsDataConceptService cb = GuiceContext.get(ClassificationsDataConceptService.class);
		
		search = search.builder()
		               .withName(name)
		               .inActiveRange(enterprise, identityToken)
		               .inDateRange()
		               .canRead(enterprise, identityToken)
		               .withEnterprise(enterprise)
		               .get()
		               .orElse(null);
		if(search != null)
		return search;
		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(enterprise);
		cb.getNoConcept(enterprise, identityToken);
		return create(name, name, cb.getNoConcept(enterprise, identityToken), activityMasterSystem, (short) 0, identityToken);
	}
	
	@CacheResult(cacheName = "ClassificationFindWithSimpleStringWithConcept")
	@Override
	public IClassification<?> find(@CacheKey String name, @CacheKey IClassificationDataConcept<?> concept, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		Classification search = new Classification();
		ClassificationsDataConceptService cb = GuiceContext.get(ClassificationsDataConceptService.class);
		search = search.builder()
		               .findByNameAndConcept(name, (ClassificationDataConcept) concept)
		               .inActiveRange(enterprise, identityToken)
		               .inDateRange()
		               .canRead(enterprise, identityToken)
		               .withEnterprise(enterprise)
		               .get()
		               .orElseThrow();
		return search;
	}
	
	
	@CacheResult(cacheName = "ClassificationFindWithSimpleStringWithConceptValue")
	@Override
	public IClassification<?> find(@CacheKey String name, @CacheKey IClassificationDataConceptValue<?> concept, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		Classification search = new Classification();
		ClassificationsDataConceptService cb = GuiceContext.get(ClassificationsDataConceptService.class);
		ClassificationDataConcept cdc = cb.find(concept, enterprise, identityToken);
		search = search.builder()
		               .findByNameAndConcept(name, cdc)
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
	
	@CacheResult(cacheName = "GetNoClassification")
	@Override
	public IClassification<?> getNoClassification(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		ClassificationService service = GuiceContext.get(ClassificationService.class);
		return service.find(NoClassification,
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
