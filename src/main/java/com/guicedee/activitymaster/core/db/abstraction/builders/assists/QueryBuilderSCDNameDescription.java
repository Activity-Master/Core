package com.guicedee.activitymaster.core.db.abstraction.builders.assists;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IContainsNameDescriptionQueryBuilder;

import java.io.Serializable;

public abstract class QueryBuilderSCDNameDescription<J extends QueryBuilderSCDNameDescription<J, E, I, S>,
		                                                    E extends WarehouseSCDNameDescriptionTable<E, J, I, S>,
		                                                    I extends Serializable,
		                                                    S extends WarehouseSecurityTable>
		extends QueryBuilderTable<J, E, I, S>
		implements IContainsNameDescriptionQueryBuilder<J, E, I>
{

}
