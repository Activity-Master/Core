package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConcept;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;

import java.util.UUID;

public interface IClassificationDataConceptService
{

	ClassificationDataConcept getGlobalConcept(Enterprise enterprise, UUID... identityToken);

	ClassificationDataConcept findConcept(IDataConceptValue<?> name,  Enterprise enterprise, UUID... identityToken);

	ClassificationDataConcept getNoConcept(Enterprise enterprise, UUID... identityToken);

	ClassificationDataConcept getSecurityHierarchyConcept(Enterprise enterprise, UUID... identityToken);
}
