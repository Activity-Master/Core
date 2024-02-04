package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseTable;
public abstract class QueryBuilderTable<J extends QueryBuilderTable<J, E, I>,
		E extends WarehouseTable<E, J, I>,
		I extends java.lang.String>
		extends QueryBuilderSCD<J, E, I>
		implements IQueryBuilderDefault<J, E, I>,
		           IQueryBuilderEnterprise<J, E, I>,
		           IQueryBuilderFlags<J, E, I>
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
