package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IContainsClassificationsQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.activitymaster.core.implementations.InvolvedPartyService;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IIdentificationType;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.persistence.criteria.JoinType;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.Equals;

public class InvolvedPartyQueryBuilder
		extends QueryBuilderTable<InvolvedPartyQueryBuilder, InvolvedParty, java.util.UUID, InvolvedPartySecurityToken>
		implements IContainsClassificationsQueryBuilder<InvolvedPartyQueryBuilder, InvolvedParty, java.util.UUID, InvolvedPartyXClassification>
{
	
	public InvolvedPartyQueryBuilder findByIdentificationType(ISystems<?> system, IIdentificationType<?> idType)
	{
		return findByIdentificationType(system, idType, null);
	}
	
	public InvolvedPartyQueryBuilder findByIdentificationType(ISystems<?> system, IIdentificationType<?> idType, String value, UUID... identityTokens)
	{
		InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder joinTableQueryBuilder = new InvolvedPartyXInvolvedPartyIdentificationType().builder();
		InvolvedPartyIdentificationType type = (InvolvedPartyIdentificationType) GuiceContext.get(InvolvedPartyService.class)
		                                                                                     .findIdentificationType(idType, system, identityTokens);
		
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
	public InvolvedPartyQueryBuilder withClassification(Classification classification, String value)
	{
		JoinExpression joinExpression = new JoinExpression();
		InvolvedPartyXClassificationQueryBuilder builder =
				new InvolvedPartyXClassification()
						.builder()
						.inActiveRange(classification.getEnterpriseID())
						.inDateRange()
						.where(InvolvedPartyXClassification_.classificationID, Equals, classification);
		if (!Strings.isNullOrEmpty(value))
		{
			builder.where(InvolvedPartyXClassification_.value, Equals, value);
		}
		
		join(InvolvedParty_.classifications,
				builder,
				JoinType.INNER, joinExpression);
		return this;
	}
}
