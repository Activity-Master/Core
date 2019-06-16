package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.*;
import com.armineasy.activitymaster.activitymaster.implementations.InvolvedPartyService;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IIdentificationType;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.jwebmp.entityassist.querybuilder.builders.JoinExpression;
import com.jwebmp.guicedinjection.GuiceContext;
import com.google.common.base.Strings;

import javax.persistence.criteria.JoinType;

import java.util.UUID;

import static com.jwebmp.entityassist.enumerations.Operand.*;

public class InvolvedPartyQueryBuilder
		extends QueryBuilder<InvolvedPartyQueryBuilder, InvolvedParty, Long, InvolvedPartySecurityToken>
{

	public InvolvedPartyQueryBuilder findByIdentificationType(IEnterprise<?> enterprise, IIdentificationType<?> idType)
	{
		return findByIdentificationType(enterprise, idType, null);
	}

	public InvolvedPartyQueryBuilder findByIdentificationType(IEnterprise<?> enterprise, IIdentificationType<?> idType, String value, UUID... identityTokens)
	{
		InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder joinTableQueryBuilder = new InvolvedPartyXInvolvedPartyIdentificationType().builder();
		InvolvedPartyIdentificationType type = (InvolvedPartyIdentificationType) GuiceContext.get(InvolvedPartyService.class)
		                                                                                     .findIdentificationType(idType, enterprise, identityTokens);

		joinTableQueryBuilder.where(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, Equals, type);
		if (value != null)
		{
			joinTableQueryBuilder.withValue(value);
		}
		joinTableQueryBuilder.inDateRange();
		joinTableQueryBuilder.inActiveRange(enterprise, identityTokens);

		join(InvolvedParty_.identities, joinTableQueryBuilder, JoinType.INNER);

		inActiveRange(enterprise);
		inDateRange();

		return this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
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
