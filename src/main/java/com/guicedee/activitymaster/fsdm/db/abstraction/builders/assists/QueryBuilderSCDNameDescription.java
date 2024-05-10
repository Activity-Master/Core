package com.guicedee.activitymaster.fsdm.db.abstraction.builders.assists;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderTable;

public abstract class QueryBuilderSCDNameDescription<
		J extends QueryBuilderSCDNameDescription<J, E, I,QS>,
		E extends WarehouseSCDNameDescriptionTable<E, J, I,?>,
		I extends java.lang.String,
		QS extends QueryBuilderSecurities<QS,?,?>
		>
		extends QueryBuilderTable<J, E, I,QS>
		implements IQueryBuilderNamesAndDescriptions<J, E, I>
{

}
