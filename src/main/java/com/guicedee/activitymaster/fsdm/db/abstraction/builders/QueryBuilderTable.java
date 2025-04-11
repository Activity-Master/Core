package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseTable;

import java.io.Serializable;

public abstract class QueryBuilderTable<
		J extends QueryBuilderTable<J, E, I,QS>,
		E extends WarehouseTable<E, J, I,?>,
		I extends Serializable,
		QS extends QueryBuilderSecurities<QS,?,I>
		>
		extends QueryBuilderCore<J, E, I>
{
	@Override
	public boolean onCreate(E entity)
	{
		if (entity.getOriginalSourceSystemUniqueID() == null)
		{
			entity.setOriginalSourceSystemUniqueID("");
		}
		
		return super.onCreate(entity);
	}
}
