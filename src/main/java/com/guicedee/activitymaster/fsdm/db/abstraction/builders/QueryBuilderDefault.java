package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.entityassist.querybuilder.QueryBuilderSCD;
import com.google.inject.persist.Transactional;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderClassifications;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseBaseTable;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;


public abstract class QueryBuilderDefault<J extends QueryBuilderDefault<J, E, I>,
		E extends WarehouseBaseTable<E, J, I>,
		I extends java.lang.String>
		extends QueryBuilderSCD<J, E, I>
		implements IQueryBuilderClassifications<J, E, I>
{
	public QueryBuilderDefault()
	{
		//setRunDetached(true);
		setReturnFirst(true);
		//setUseDirectConnection(true);
		//	setDetach(true);
	}
	
	@Override
	public boolean onCreate(E entity)
	{
		if (entity.getId() == null)
		{
			//noinspection unchecked
			entity.setId((I) UUID.randomUUID().toString());
		}
		return super.onCreate(entity);
	}
	
	@Override
	@Transactional()
	public @NotNull J persist(E entity)
	{
		return super.persist(entity);
	}
	
	@Override
	public EntityManager getEntityManager()
	{
		return com.guicedee.client.IGuiceContext.get(EntityManager.class);
	}
	
	@Override
	public boolean isIdGenerated()
	{
		return false;
	}
}
