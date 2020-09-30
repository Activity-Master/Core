package com.guicedee.activitymaster.core.db.abstraction.builders.assists;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseNameDescriptionTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderCore;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IContainsNameDescriptionQueryBuilder;

import java.io.Serializable;

public abstract class QueryBuilderNameDescription<J extends QueryBuilderNameDescription<J, E, I, S>,
		                                                 E extends WarehouseNameDescriptionTable<E, J, I, S>,
		                                                 I extends Serializable,
		                                                 S extends WarehouseSecurityTable>
		extends QueryBuilderCore<J, E, I, S>
		implements IContainsNameDescriptionQueryBuilder<J,E,I>
{

}
