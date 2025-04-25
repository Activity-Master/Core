package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.entityassist.querybuilder.QueryBuilderSCD;
import com.google.inject.ProvisionException;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderDefault;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseBaseTable;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;


public abstract class QueryBuilderDefault<J extends QueryBuilderDefault<J, E, I>,
		E extends WarehouseBaseTable<E, J, I>,
		I extends UUID>
		extends QueryBuilderSCD<J, E, I>
		implements IQueryBuilderDefault<J, E, I>
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
			entity.setId((I) UUID.randomUUID());
		}
		return super.onCreate(entity);
	}
	
	@Override
	public @NotNull J persist(E entity)
	{
		return super.persist(entity);
	}
	
	@Override
	public EntityManager getEntityManager()
	{
		try
		{
			return com.guicedee.client.IGuiceContext.get(EntityManager.class);
		}catch (ProvisionException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean isIdGenerated()
	{
		return false;
	}
}
