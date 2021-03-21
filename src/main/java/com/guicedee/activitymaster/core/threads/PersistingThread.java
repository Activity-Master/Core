package com.guicedee.activitymaster.core.threads;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.client.services.annotations.ActivityMasterDB;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersistingThread
		implements Callable<Object>
{
	private static final Logger log = Logger.getLogger(PersistingThread.class.getName());
	private final List<BaseEntity> entities = new ArrayList<>();

	@Override
	public Object call() throws Exception
	{
		run();
		return null;
	}

	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	void run()
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
