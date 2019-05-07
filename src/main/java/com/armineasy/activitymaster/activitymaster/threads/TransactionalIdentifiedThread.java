package com.armineasy.activitymaster.activitymaster.threads;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

public abstract class TransactionalIdentifiedThread extends IdentifiedThread<TransactionalIdentifiedThread>
{
	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public abstract void perform();

}
