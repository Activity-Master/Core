package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangementTypesQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.assists.QueryBuilderSCDNameDescription;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementType;

import java.util.UUID;

public class ArrangementTypeQueryBuilder
		extends QueryBuilderSCDNameDescription<ArrangementTypeQueryBuilder,ArrangementType, UUID>
		implements IArrangementTypesQueryBuilder<ArrangementTypeQueryBuilder,ArrangementType>
{

}
