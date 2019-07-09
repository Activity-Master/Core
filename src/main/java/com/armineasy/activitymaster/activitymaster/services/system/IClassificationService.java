package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.dto.IClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
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
