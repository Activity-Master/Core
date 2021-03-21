package com.guicedee.activitymaster.core.db.abstraction.builders;

import com.entityassist.querybuilder.QueryBuilderSCD;
import com.guicedee.activitymaster.client.services.annotations.ActivityMasterDB;
import com.guicedee.activitymaster.client.services.builders.IQueryBuilderClassifications;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.EntityManager;

import java.util.UUID;

public abstract class QueryBuilderDefault<J extends QueryBuilderDefault<J, E, I>,
		E extends WarehouseBaseTable<E, J, I>,
		I extends java.util.UUID>
		extends QueryBuilderSCD<J, E, I>
		implements IQueryBuilderClassifications<J, E, I>
{
	public QueryBuilderDefault()
	{
		setRunDetached(true);
		setReturnFirst(true);
		setDetach(true);
	}
	
	@Override
	public boolean onCreate(E entity)
	{
		if (entity.getId() == null)
		{
			//noinspection unchecked
			entity.setId((I) UUID.randomUUID());
		}
		return super.onCreate(entity);
	}
	
	@Override
	public EntityManager getEntityManager()
	{
		return GuiceContext.get(EntityManager.class, ActivityMasterDB.class);
	}
	
	@Override
	public boolean isIdGenerated()
	{
		return false;
	}
}
