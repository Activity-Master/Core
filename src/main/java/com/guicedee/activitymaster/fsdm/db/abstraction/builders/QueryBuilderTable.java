package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseTable;

import java.io.Serializable;
import java.util.UUID;

public abstract class QueryBuilderTable<
		J extends QueryBuilderTable<J, E, I,QS>,
		E extends WarehouseTable<E, J, I,?>,
		I extends java.util.UUID,
		QS extends QueryBuilderSecurities<QS,?,I>
		>
		extends QueryBuilderCore<J, E, I>
{
	@Override
	public boolean onCreate(E entity)
	{
		if (entity.getOriginalSourceSystemUniqueID() == null)
		{
			entity.setOriginalSourceSystemUniqueID(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"));
		}
		
		return super.onCreate(entity);
	}
}
