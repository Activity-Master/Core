package com.guicedee.activitymaster.core.services.system;

		import com.guicedee.activitymaster.core.services.dto.IClassificationDataConcept;
		import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;
		import com.guicedee.activitymaster.core.services.dto.IEnterprise;

		import java.util.UUID;

public interface IClassificationDataConceptService
{
	IClassificationDataConcept<?> getGlobalConcept(IEnterprise<?> enterprise, UUID... identityToken);

	IClassificationDataConcept<?> find(IClassificationDataConceptValue<?> name, IEnterprise<?> enterprise, UUID... identityToken);

	IClassificationDataConcept<?> getNoConcept(IEnterprise<?> enterprise, UUID... identityToken);

	IClassificationDataConcept<?> getSecurityHierarchyConcept(IEnterprise<?> enterprise, UUID... identityToken);
}
