package com.guicedee.activitymaster.core.updates;

import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IProgressable;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;

public interface ISystemUpdate extends IProgressable
{
	void update(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor);
}
