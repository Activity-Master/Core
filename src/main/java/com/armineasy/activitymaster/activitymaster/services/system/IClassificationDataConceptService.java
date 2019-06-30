package com.armineasy.activitymaster.activitymaster.services.system;

		import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConcept;
		import com.armineasy.activitymaster.activitymaster.services.dto.IClassificationDataConcept;
		import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationDataConceptValue;
		import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;

		import java.util.UUID;

public interface IClassificationDataConceptService
{
	IClassificationDataConcept<?> getGlobalConcept(IEnterprise<?> enterprise, UUID... identityToken);

	IClassificationDataConcept<?> find(IClassificationDataConceptValue<?> name, IEnterprise<?> enterprise, UUID... identityToken);

	IClassificationDataConcept<?> getNoConcept(IEnterprise<?> enterprise, UUID... identityToken);

	IClassificationDataConcept<?> getSecurityHierarchyConcept(IEnterprise<?> enterprise, UUID... identityToken);
}
