package com.guicedee.activitymaster.fsdm.db.abstraction.builders.assists;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderTable;

public abstract class QueryBuilderSCDNameDescription<J extends QueryBuilderSCDNameDescription<J, E, I>,
		E extends WarehouseSCDNameDescriptionTable<E, J, I>,
		I extends java.lang.String>
		extends QueryBuilderTable<J, E, I>
		implements IQueryBuilderNamesAndDescriptions<J, E, I>
{

}
