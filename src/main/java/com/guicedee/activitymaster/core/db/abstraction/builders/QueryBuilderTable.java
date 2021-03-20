package com.guicedee.activitymaster.core.db.abstraction.builders;

import com.guicedee.activitymaster.client.services.builders.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;

import static com.guicedee.guicedinjection.json.StaticStrings.*;

public abstract class QueryBuilderTable<J extends QueryBuilderTable<J, E, I>,
		E extends WarehouseTable<E, J, I>,
		I extends java.util.UUID>
		extends QueryBuilderSCD<J, E, I>
		implements IQueryBuilderDefault<J,E,I>,
		           IQueryBuilderEnterprise<J,E,I>,
		           IQueryBuilderFlags<J,E,I>
{
	@Override
	public boolean onCreate(E entity)
	{
		if (entity.getOriginalSourceSystemUniqueID() == null)
		{ entity.setOriginalSourceSystemUniqueID(STRING_EMPTY); }
		return super.onCreate(entity);
	}
}
