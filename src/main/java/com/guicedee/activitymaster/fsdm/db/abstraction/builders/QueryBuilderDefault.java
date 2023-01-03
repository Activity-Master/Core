package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.entityassist.querybuilder.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderClassifications;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseBaseTable;
import com.guicedee.guicedpersistence.db.annotations.Transactional;
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
		setReturnFirst(true);
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
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public @NotNull J persist(E entity)
	{
		return super.persist(entity);
	}
}
