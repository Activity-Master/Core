package com.guicedee.activitymaster.core.db.abstraction.builders;

import com.guicedee.activitymaster.client.services.builders.IQueryBuilderDefault;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSCDTable;

public abstract class QueryBuilderSCD<J extends QueryBuilderSCD<J, E, I>,
		E extends WarehouseSCDTable<E, J, I>,
		I extends java.util.UUID>
		extends QueryBuilderCore<J, E, I>
		implements IQueryBuilderDefault<J,E,I>
	//	implements ISecurityEnabledQueryBuilder<J, E, I>
{
	
}
