package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderFlags;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;

public abstract class QueryBuilderSCD<
		J extends QueryBuilderSCD<J, E, I,QS>,
		E extends WarehouseSCDTable<E, J, I,?>,
		I extends java.lang.String,
		QS extends QueryBuilderSecurities<QS,?,I>>
		extends QueryBuilderTable<J, E, I,QS>
		implements IQueryBuilderEnterprise<J, E, I>,
		           IQueryBuilderFlags<J, E, I>
		//	implements ISecurityEnabledQueryBuilder<J, E, I>
{
	
}
