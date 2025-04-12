package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedPartyNameTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyNameType;

import java.util.UUID;

public class InvolvedPartyNameTypeQueryBuilder
		extends QueryBuilderSCD<InvolvedPartyNameTypeQueryBuilder, InvolvedPartyNameType, UUID,
		InvolvedPartyNameTypeSecurityTokenQueryBuilder>
		implements IInvolvedPartyNameTypeQueryBuilder<InvolvedPartyNameTypeQueryBuilder, InvolvedPartyNameType>,
		           IQueryBuilderNamesAndDescriptions<InvolvedPartyNameTypeQueryBuilder, InvolvedPartyNameType, UUID>
{

}
