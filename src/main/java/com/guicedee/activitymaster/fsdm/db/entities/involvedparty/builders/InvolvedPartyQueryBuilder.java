package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedPartyQueryBuilder;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import jakarta.persistence.criteria.JoinType;

import static com.entityassist.enumerations.Operand.*;

public class InvolvedPartyQueryBuilder
		extends QueryBuilderSCD<InvolvedPartyQueryBuilder, InvolvedParty, String,InvolvedPartySecurityTokenQueryBuilder>
		implements IInvolvedPartyQueryBuilder<InvolvedPartyQueryBuilder, InvolvedParty>
{
	@Inject
	private IInvolvedPartyService<?> involvedPartyService;
	
	@Override
	public InvolvedPartyQueryBuilder findByIdentificationType(String idType, String value, ISystems<?, ?> system, java.util.UUID... identityTokens)
	{
		InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder joinTableQueryBuilder = new InvolvedPartyXInvolvedPartyIdentificationType().builder();
		
		InvolvedPartyIdentificationType type = (InvolvedPartyIdentificationType)
				involvedPartyService.findInvolvedPartyIdentificationType(idType, system, identityTokens);
		
		joinTableQueryBuilder.where(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, Equals, type);
		if (value != null)
		{
			joinTableQueryBuilder.withValue(value);
		}
		joinTableQueryBuilder.inDateRange();
		joinTableQueryBuilder.inActiveRange();
		
		join(InvolvedParty_.identities, joinTableQueryBuilder, JoinType.INNER);
		
		inActiveRange();
		inDateRange();
		
		return this;
	}
	
	@Override
	public InvolvedPartyQueryBuilder findByType(String idType, String value, ISystems<?, ?> system, java.util.UUID... identityTokens)
	{
		InvolvedPartyXInvolvedPartyTypeQueryBuilder joinTableQueryBuilder = new InvolvedPartyXInvolvedPartyType().builder();
		InvolvedPartyType type = (InvolvedPartyType) involvedPartyService.findType(idType, system, identityTokens);
		
		joinTableQueryBuilder.where(InvolvedPartyXInvolvedPartyType_.involvedPartyTypeID, Equals, type);
		if (value != null)
		{
			joinTableQueryBuilder.withValue(value);
		}
		joinTableQueryBuilder.inDateRange();
		joinTableQueryBuilder.inActiveRange();
		
		join(InvolvedParty_.types, joinTableQueryBuilder, JoinType.INNER);
		
		inActiveRange();
		inDateRange();
		return this;
	}
	
	@Override
	public InvolvedPartyQueryBuilder findByTypeAll(String idType, String value, ISystems<?, ?> system, java.util.UUID... identityTokens)
	{
		InvolvedPartyXInvolvedPartyTypeQueryBuilder joinTableQueryBuilder = new InvolvedPartyXInvolvedPartyType().builder();
		InvolvedPartyType type = (InvolvedPartyType) involvedPartyService.findType(idType, system, identityTokens);
		
		joinTableQueryBuilder.where(InvolvedPartyXInvolvedPartyType_.involvedPartyTypeID, Equals, type);
		if (value != null)
		{
			joinTableQueryBuilder.withValue(value);
		}
		join(InvolvedParty_.types, joinTableQueryBuilder, JoinType.INNER);
		return this;
	}
	
	@jakarta.validation.constraints.NotNull
	@Override
	public InvolvedPartyQueryBuilder withClassification(IClassification<?, ?> classification, String value, ISystems<?, ?> system)
	{
		JoinExpression joinExpression = new JoinExpression();
		InvolvedPartyXClassificationQueryBuilder builder =
				new InvolvedPartyXClassification()
						.builder()
						.inActiveRange()
						.inDateRange()
						.withValue(value)
						.withClassification(classification.getName(), system);
		
		join(InvolvedParty_.classifications,
				builder,
				JoinType.INNER, joinExpression);
		return this;
	}
}
