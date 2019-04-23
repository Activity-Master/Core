package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IActiveFlagService
{
	List<ActiveFlag> findActiveRange(Enterprise enterprise, UUID... identifyingToken);

	List<ActiveFlag> getVisibleRange(Enterprise enterprise, UUID... identifyingToken);

	List<ActiveFlag> getRemovedRange(Enterprise enterprise, UUID... identifyingToken);

	List<ActiveFlag> getArchiveRange(Enterprise enterprise, UUID... identifyingToken);

	List<ActiveFlag> getHighlightedRange(Enterprise enterprise, UUID... identifyingToken);

	ActiveFlag getActiveFlag(Enterprise enterprise, UUID... identifyingToken);

	ActiveFlag getArchivedFlag(Enterprise enterprise, UUID... identifyingToken);

	ActiveFlag getDeletedFlag(Enterprise enterprise, UUID... identifyingToken);

	Optional<ActiveFlag> findFlagByName(com.jwebmp.entityassist.enumerations.ActiveFlag flag, Enterprise enterprise, UUID... identifyingToken);
}
