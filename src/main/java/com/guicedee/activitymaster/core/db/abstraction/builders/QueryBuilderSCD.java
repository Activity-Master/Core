package com.guicedee.activitymaster.core.db.abstraction.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.ISecurityEnabledQueryBuilder;

import java.io.Serializable;

public abstract class QueryBuilderSCD<J extends QueryBuilderSCD<J, E, I, S>,
		E extends WarehouseSCDTable<E, J, I, S>,
		I extends Serializable,
		S extends WarehouseSecurityTable>
		extends QueryBuilderCore<J, E, I, S>
		implements ISecurityEnabledQueryBuilder<J, E, I, S>
{
	
}
