package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;

import java.util.UUID;

public interface IClassificationService<J extends IClassificationService<J>>
{
	IClassification<?> create(IClassificationValue<?> concept,
	                          ISystems system, IClassificationValue<?> parent,
	                          UUID... identityToken);

	IClassification<?> create(IClassificationValue<?> concept,
	                          ISystems system,
	                          UUID... identityToken);

	IClassification<?> create(IClassificationValue<?> concept,
	                          ISystems system,
	                          Short sequenceNumber, UUID... identityToken);

	IClassification<?> create(IClassificationValue<?> concept,
	                          ISystems system,
	                          Short sequenceNumber, IClassificationValue<?> parent, UUID... identityToken);

	@SuppressWarnings("Duplicates")
	IClassification<?> find(IClassificationValue<?> name, IEnterprise<?> enterprise, UUID... identityToken);

	IClassification<?> getHierarchyType( IEnterprise<?> enterprise, UUID... identityToken);

	IClassification<?> getIdentityType( IEnterprise<?> enterprise,UUID... identityToken);
}
