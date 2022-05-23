package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderDefault;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;

public abstract class QueryBuilderSCD<J extends QueryBuilderSCD<J, E, I>,
		E extends WarehouseSCDTable<E, J, I>,
		I extends java.lang.String>
		extends QueryBuilderCore<J, E, I>
		implements IQueryBuilderDefault<J, E, I>
		//	implements ISecurityEnabledQueryBuilder<J, E, I>
{
	
}
