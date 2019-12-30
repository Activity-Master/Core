package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.core.services.dto.IClassificationDataConcept;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;

import java.util.UUID;

public interface IClassificationDataConceptService<J extends IClassificationDataConceptService<J>>
{
	ClassificationDataConcept createDataConcept(IClassificationDataConceptValue<?> name,
	                                            String description,
	                                            ISystems<?> system, UUID... identityToken);

	IClassificationDataConcept<?> getGlobalConcept(IEnterprise<?> enterprise, UUID... identityToken);

	IClassificationDataConcept<?> find(IClassificationDataConceptValue<?> name, IEnterprise<?> enterprise, UUID... identityToken);

	IClassificationDataConcept<?> getNoConcept(IEnterprise<?> enterprise, UUID... identityToken);

	IClassificationDataConcept<?> getSecurityHierarchyConcept(IEnterprise<?> enterprise, UUID... identityToken);
}
