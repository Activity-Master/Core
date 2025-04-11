package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseCoreTable;

import java.io.Serializable;

public abstract class QueryBuilderCore<J extends QueryBuilderCore<J, E, I>,
		E extends WarehouseCoreTable<E, J, I,?>,
		I extends Serializable>
		extends QueryBuilderDefault<J, E, I>
{
}
