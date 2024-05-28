package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangementQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.Arrangement;

public class ArrangementQueryBuilder
		extends QueryBuilderSCD<ArrangementQueryBuilder, Arrangement, String,ArrangementSecurityTokenQueryBuilder>
		implements IArrangementQueryBuilder<ArrangementQueryBuilder, Arrangement>
{

}
