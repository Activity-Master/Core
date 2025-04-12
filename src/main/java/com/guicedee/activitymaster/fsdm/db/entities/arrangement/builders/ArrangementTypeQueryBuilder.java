package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangementTypesQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementType;

import java.util.UUID;

public class ArrangementTypeQueryBuilder
		extends QueryBuilderSCD<ArrangementTypeQueryBuilder, ArrangementType, UUID,ArrangementTypeSecurityTokenQueryBuilder>
		implements IArrangementTypesQueryBuilder<ArrangementTypeQueryBuilder, ArrangementType>
{

}
