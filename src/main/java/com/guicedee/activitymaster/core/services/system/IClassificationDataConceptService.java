package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.core.services.dto.IClassificationDataConcept;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;

import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import java.util.UUID;

public interface IClassificationDataConceptService<J extends IClassificationDataConceptService<J>>
{
	ClassificationDataConcept createDataConcept(IClassificationDataConceptValue<?> name,
	                                            String description,
	                                            ISystems<?> system, UUID... identityToken);

	IClassificationDataConcept<?> getGlobalConcept(ISystems<?> system, UUID... identityToken);

	IClassificationDataConcept<?> find(IClassificationDataConceptValue<?> name, ISystems<?> system, UUID... identityToken);

	ClassificationDataConcept find(String name, ISystems<?> system,UUID... identityToken);
	
	IClassificationDataConcept<?> getNoConcept(ISystems<?> system, UUID... identityToken);

	IClassificationDataConcept<?> getSecurityHierarchyConcept(ISystems<?> system, UUID... identityToken);
}
