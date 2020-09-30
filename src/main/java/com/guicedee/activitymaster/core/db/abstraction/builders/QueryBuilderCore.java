package com.guicedee.activitymaster.core.db.abstraction.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.ISecurityEnabledQueryBuilder;

import java.io.Serializable;

public abstract class QueryBuilderCore<J extends QueryBuilderCore<J, E, I, S>,
		                                  E extends WarehouseCoreTable<E, J, I, S>,
		                                  I extends Serializable,
		                                  S extends WarehouseSecurityTable>
		extends QueryBuilderDefault<J, E, I>
		implements ISecurityEnabledQueryBuilder<J,E,I,S>
{
}
