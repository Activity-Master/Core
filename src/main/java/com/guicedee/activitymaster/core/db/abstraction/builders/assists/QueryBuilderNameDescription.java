package com.guicedee.activitymaster.core.db.abstraction.builders.assists;

import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseNameDescriptionTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderCore;

public abstract class QueryBuilderNameDescription<J extends QueryBuilderNameDescription<J, E, I>,
		                                                 E extends WarehouseNameDescriptionTable<E, J, I>,
		                                                 I extends java.util.UUID>
		extends QueryBuilderCore<J, E, I>
{

}
