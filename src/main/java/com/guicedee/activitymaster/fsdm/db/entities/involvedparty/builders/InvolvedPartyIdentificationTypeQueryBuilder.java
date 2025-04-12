package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyIdentificationType;

import java.util.UUID;

public class InvolvedPartyIdentificationTypeQueryBuilder
		extends QueryBuilderSCD<InvolvedPartyIdentificationTypeQueryBuilder, InvolvedPartyIdentificationType, UUID,
				InvolvedPartyIdentificationTypeSecurityTokenQueryBuilder>
		implements IInvolvedPartyIdentificationTypeQueryBuilder<InvolvedPartyIdentificationTypeQueryBuilder, InvolvedPartyIdentificationType>,
		           IQueryBuilderNamesAndDescriptions<InvolvedPartyIdentificationTypeQueryBuilder,InvolvedPartyIdentificationType,UUID>
{

}
