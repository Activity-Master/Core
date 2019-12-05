package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.dto.IActiveFlag;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IActiveFlagService
{
	List<IActiveFlag<?>> findActiveRange(IEnterprise<?> enterprise, UUID... identifyingToken);

	List<IActiveFlag<?>> getVisibleRange(IEnterprise<?> enterprise, UUID... identifyingToken);

	List<IActiveFlag<?>> getRemovedRange(IEnterprise<?> enterprise, UUID... identifyingToken);

	List<IActiveFlag<?>> getArchiveRange(IEnterprise<?> enterprise, UUID... identifyingToken);

	List<IActiveFlag<?>> getHighlightedRange(IEnterprise<?> enterprise, UUID... identifyingToken);

	IActiveFlag<?> getActiveFlag(IEnterprise<?> enterprise, UUID... identifyingToken);

	IActiveFlag<?> getArchivedFlag(IEnterprise<?> enterprise, UUID... identifyingToken);

	IActiveFlag<?> getDeletedFlag(IEnterprise<?> enterprise, UUID... identifyingToken);

	Optional<IActiveFlag<?>> findFlagByName(com.entityassist.enumerations.ActiveFlag flag, IEnterprise<?> enterprise, UUID... identifyingToken);
}
