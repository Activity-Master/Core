package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangementQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.Arrangement;

import java.util.UUID;

public class ArrangementQueryBuilder
		extends QueryBuilderSCD<ArrangementQueryBuilder, Arrangement, UUID,ArrangementSecurityTokenQueryBuilder>
		implements IArrangementQueryBuilder<ArrangementQueryBuilder, Arrangement>
{

}
