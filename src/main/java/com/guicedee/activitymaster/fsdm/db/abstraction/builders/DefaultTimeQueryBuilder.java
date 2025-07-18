package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.entityassist.BaseEntity;
import com.entityassist.querybuilder.QueryBuilder;
import com.guicedee.client.IGuiceContext;
import jakarta.persistence.EntityManager;
import org.hibernate.reactive.mutiny.Mutiny;

import java.io.Serializable;
import java.util.UUID;

public abstract class DefaultTimeQueryBuilder<
		J extends DefaultTimeQueryBuilder<J, E, I>,
		E extends BaseEntity<E, J, I>,
		I extends Serializable>
		extends QueryBuilder<J, E, I>
{
	public DefaultTimeQueryBuilder()
	{
	//	setRunDetached(true);
		//setReturnFirst(true);
	//	setUseDirectConnection(true);
	//	setDetach(true);
	}

	@Override
	public Mutiny.Session getEntityManager()
	{
		return IGuiceContext.get(Mutiny.SessionFactory.class).getCurrentSession();
	}

	@Override
	public boolean isIdGenerated()
	{
		return false;
	}
	
}
