package com.armineasy.activitymaster.activitymaster.threads;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.jwebmp.entityassist.BaseEntity;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;

@Log
public class PersistingThread
		implements Callable<Object>
{
	private final List<BaseEntity> entities = new ArrayList<>();

	@Override
	public Object call() throws Exception
	{
		perform();
		return null;
	}

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	void perform()
	{
		for (BaseEntity entity : entities)
		{
			try
			{
				entity.persist();
			}
			catch (Throwable T)
			{
				log.log(Level.WARNING, "Unable to persist entity : " + entity, T);
			}
		}
	}

	public PersistingThread addEntity(BaseEntity entity)
	{
		entities.add(entity);
		return this;
	}

	public static PersistingThread newInstance()
	{
		return GuiceContext.get(PersistingThread.class);
	}
}
