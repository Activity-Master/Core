package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.guicedee.activitymaster.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.client.services.builders.warehouse.party.IInvolvedPartyQueryBuilder;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.core.InvolvedPartyService;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.criteria.JoinType;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class InvolvedPartyQueryBuilder
		extends QueryBuilderTable<InvolvedPartyQueryBuilder, InvolvedParty, java.util.UUID>
		implements IInvolvedPartyQueryBuilder<InvolvedPartyQueryBuilder,InvolvedParty>
{
	@Override
	public InvolvedPartyQueryBuilder findByIdentificationType(ISystems<?,?> system, String idType)
	{
		return findByIdentificationType(system, idType, null);
	}
	
	@Override
	public InvolvedPartyQueryBuilder findByIdentificationType(ISystems<?,?> system, String idType, String value, UUID... identityTokens)
	{
		InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder joinTableQueryBuilder = new InvolvedPartyXInvolvedPartyIdentificationType().builder();
		InvolvedPartyIdentificationType type = (InvolvedPartyIdentificationType) GuiceContext.get(InvolvedPartyService.class)
		                                                                                     .findInvolvedPartyIdentificationType(idType, system, identityTokens);
		
		joinTableQueryBuilder.where(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, Equals, type);
		if (value != null)
		{
			joinTableQueryBuilder.withValue(value);
		}
		joinTableQueryBuilder.inDateRange();
		joinTableQueryBuilder.inActiveRange(system, identityTokens);
		
		join(InvolvedParty_.identities, joinTableQueryBuilder, JoinType.INNER);
		
		inActiveRange(system);
		inDateRange();
		
		return this;
	}
	
	@SuppressWarnings("unchecked")
	@jakarta.validation.constraints.NotNull
	@Override
	public InvolvedPartyQueryBuilder withClassification(IClassification<?,?> classification, String value,ISystems<?,?> system)
	{
		JoinExpression joinExpression = new JoinExpression();
		InvolvedPartyXClassificationQueryBuilder builder =
				new InvolvedPartyXClassification()
						.builder()
						.inActiveRange(classification.getEnterpriseID())
						.inDateRange()
						.withValue(value)
						.withClassification(classification.getName(), system);

		join(InvolvedParty_.classifications,
				builder,
				JoinType.INNER, joinExpression);
		return this;
	}
}
