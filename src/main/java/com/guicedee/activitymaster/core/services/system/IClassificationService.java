package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.services.dto.IClassificationDataConcept;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;

import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import java.util.UUID;

public interface IClassificationService<J extends IClassificationService<J>>
{
	IClassification<?> create(IClassificationValue<?> concept,
	                          ISystems<?> system, IClassificationValue<?> parent,
	                          UUID... identityToken);

	IClassification<?> create(IClassificationValue<?> concept,
	                          ISystems<?> system,
	                          UUID... identityToken);

	IClassification<?> create(IClassificationValue<?> concept,
	                          ISystems<?> system,
	                          Short sequenceNumber, UUID... identityToken);

	IClassification<?> create(IClassificationValue<?> concept,
	                          ISystems<?> system,
	                          Short sequenceNumber, IClassificationValue<?> parent, UUID... identityToken);

	IClassification<?> create(String name, String description, IClassificationDataConceptValue<?> conceptName,
	                          ISystems<?> system,
	                          Short sequenceNumber, IClassificationValue<?> parent, UUID... identityToken);

	IClassification<?> create(String name, String description, IClassificationDataConceptValue<?> conceptName,
							  ISystems<?> system,
							  Short sequenceNumber, String parent, UUID... identityToken);

	IClassification<?> create(String name, String description, IClassificationDataConceptValue<?> conceptName,
							  ISystems<?> system,
							  Short sequenceNumber, IClassification<?> parent, UUID... identityToken);
	
	IClassification<?> create(String name, String description, IClassificationDataConceptValue<?> conceptName,
	                          IEnterprise<?> system,
	                          Short sequenceNumber, IClassification<?> parent, UUID... identityToken);

	IClassification<?> create(String name, String description, IClassificationDataConceptValue<?> conceptName,
							  ISystems<?> system,
							  Short sequenceNumber, UUID... identityToken);
	
	IClassification<?> find(IClassificationValue<?> name, ISystems<?> system, UUID... identityToken);
	
	@SuppressWarnings("Duplicates")
    IClassification<?> find(IClassificationValue<?> name, IEnterprise<?> enterprise, UUID... identityToken);
	
	//@CacheResult(cacheName = "ClassificationFindWithSimpleString")
	IClassification<?> find( String name, ISystems<?> system, UUID... identityToken);
	
	IClassification<?> find(String name, IEnterprise<?> enterprise, UUID... identityToken);
	
	IClassification<?> findOrCreate( String name, IEnterprise<?> enterprise, UUID... identityToken);
	
	IClassification<?> find(String name, IClassificationDataConcept<?> concept, IEnterprise<?> enterprise, UUID... identityToken);

    IClassification<?> find(String name, IClassificationDataConceptValue<?> concept, IEnterprise<?> enterprise, UUID... identityToken);

    IClassification<?> getHierarchyType(IEnterprise<?> enterprise, UUID... identityToken);
	
	IClassification<?> getNoClassification(IEnterprise<?> enterprise, UUID... identityToken);
	
	IClassification<?> getIdentityType(IEnterprise<?> enterprise, UUID... identityToken);
}
