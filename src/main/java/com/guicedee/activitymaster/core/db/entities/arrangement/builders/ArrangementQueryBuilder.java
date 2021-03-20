package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.client.services.builders.warehouse.arrangements.IArrangementQueryBuilder;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.Arrangement;

public class ArrangementQueryBuilder
		extends QueryBuilderTable<ArrangementQueryBuilder, Arrangement, java.util.UUID>
		implements IArrangementQueryBuilder<ArrangementQueryBuilder,Arrangement>
{

}
