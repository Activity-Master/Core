package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConcept;
import com.armineasy.activitymaster.activitymaster.services.IClassificationDataConceptValue;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;

import java.util.UUID;

public interface IClassificationDataConceptService
{

	ClassificationDataConcept getGlobalConcept(IEnterprise<?> enterprise, UUID... identityToken);

	ClassificationDataConcept find(IClassificationDataConceptValue<?> name, IEnterprise<?> enterprise, UUID... identityToken);

	ClassificationDataConcept getNoConcept(IEnterprise<?> enterprise, UUID... identityToken);

	ClassificationDataConcept getSecurityHierarchyConcept(IEnterprise<?> enterprise, UUID... identityToken);
}
