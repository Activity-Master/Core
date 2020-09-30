package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.services.dto.IClassificationDataConcept;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
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

	@SuppressWarnings("Duplicates")
    IClassification<?> find(IClassificationValue<?> name, IEnterprise<?> enterprise, UUID... identityToken);

	IClassification<?> find(String name, IEnterprise<?> enterprise, UUID... identityToken);
	
	
	@CacheResult(cacheName = "ClassificationFindWithSimpleString")
	IClassification<?> findOrCreate(@CacheKey String name, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken);
	
	IClassification<?> find(String name, IClassificationDataConcept<?> concept, IEnterprise<?> enterprise, UUID... identityToken);

    @CacheResult(cacheName = "ClassificationFindWithSimpleStringWithConceptValue")
    IClassification<?> find(@CacheKey String name, @CacheKey IClassificationDataConceptValue<?> concept, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken);

    IClassification<?> getHierarchyType(IEnterprise<?> enterprise, UUID... identityToken);

	IClassification<?> getIdentityType( IEnterprise<?> enterprise,UUID... identityToken);
}
