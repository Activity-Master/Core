package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

public interface IActivityMasterService
{
	void loadSystems(IEnterpriseName<?> enterpriseName, IActivityMasterProgressMonitor progressMonitor);

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class,
			timeout = 5000)
	void loadUpdates(IEnterpriseName<?> enterpriseName, IActivityMasterProgressMonitor progressMonitor);
}
