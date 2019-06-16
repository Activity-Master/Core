package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IActiveFlagService
{
	List<ActiveFlag> findActiveRange(IEnterprise<?> enterprise, UUID... identifyingToken);

	List<ActiveFlag> getVisibleRange(IEnterprise<?> enterprise, UUID... identifyingToken);

	List<ActiveFlag> getRemovedRange(IEnterprise<?> enterprise, UUID... identifyingToken);

	List<ActiveFlag> getArchiveRange(IEnterprise<?> enterprise, UUID... identifyingToken);

	List<ActiveFlag> getHighlightedRange(IEnterprise<?> enterprise, UUID... identifyingToken);

	ActiveFlag getActiveFlag(IEnterprise<?> enterprise, UUID... identifyingToken);

	ActiveFlag getArchivedFlag(IEnterprise<?> enterprise, UUID... identifyingToken);

	ActiveFlag getDeletedFlag(IEnterprise<?> enterprise, UUID... identifyingToken);

	Optional<ActiveFlag> findFlagByName(com.jwebmp.entityassist.enumerations.ActiveFlag flag, IEnterprise<?> enterprise, UUID... identifyingToken);
}
