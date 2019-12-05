package com.guicedee.activitymaster.core.threads;

import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

public abstract class TransactionalIdentifiedThread extends IdentifiedThread<TransactionalIdentifiedThread>
{
	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public abstract void perform();

}
