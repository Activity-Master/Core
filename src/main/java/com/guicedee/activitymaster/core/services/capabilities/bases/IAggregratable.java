package com.guicedee.activitymaster.core.services.capabilities.bases;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderDefault;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IHasValueQueryBuilder;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface IAggregratable<J extends QueryBuilderDefault<J, E, I>,
		E extends WarehouseBaseTable<E, J, I>,
		I extends Serializable,
		T extends IClassificationValue<?>>
		extends IHasValueQueryBuilder<J, E, I>
{
	
	default List<Object[]> renderView()
	{
		return new ArrayList<>();
	}
}
