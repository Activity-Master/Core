package com.guicedee.activitymaster.fsdm.db.entities.time.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.DefaultTimeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.time.Time;
import com.guicedee.activitymaster.fsdm.db.entities.time.TimePK;
import com.guicedee.client.IGuiceContext;
import jakarta.persistence.EntityManager;
import org.hibernate.reactive.mutiny.Mutiny;

public class TimeQueryBuilder
		extends DefaultTimeQueryBuilder<TimeQueryBuilder, Time, TimePK>
{

	@Override
	public Mutiny.Session getEntityManager()
	{
		return IGuiceContext.get(Mutiny.SessionFactory.class).getCurrentSession();
	}
}
