package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedPartyTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyType;

import java.util.UUID;

public class InvolvedPartyTypeQueryBuilder
		extends QueryBuilderSCD<InvolvedPartyTypeQueryBuilder, InvolvedPartyType, UUID,
				InvolvedPartyTypeSecurityTokenQueryBuilder>
		implements IInvolvedPartyTypeQueryBuilder<InvolvedPartyTypeQueryBuilder, InvolvedPartyType>,
		           IQueryBuilderNamesAndDescriptions<InvolvedPartyTypeQueryBuilder,InvolvedPartyType,java.util.UUID>
{

}
