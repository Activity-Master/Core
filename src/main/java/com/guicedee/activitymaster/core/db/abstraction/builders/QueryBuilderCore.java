package com.guicedee.activitymaster.core.db.abstraction.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;

public abstract class QueryBuilderCore<J extends QueryBuilderCore<J, E, I>,
		                                  E extends WarehouseCoreTable<E, J, I>,
		                                  I extends java.util.UUID>
		extends QueryBuilderDefault<J, E, I>
		
{
}
