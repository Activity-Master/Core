package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.*;
import com.armineasy.activitymaster.activitymaster.implementations.InvolvedPartyService;
import com.armineasy.activitymaster.activitymaster.services.IIdentificationType;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.persistence.criteria.JoinType;

import java.util.UUID;

import static com.jwebmp.entityassist.enumerations.Operand.*;

public class InvolvedPartyQueryBuilder
		extends QueryBuilder<InvolvedPartyQueryBuilder, InvolvedParty, Long, InvolvedPartySecurityToken>
{

	public InvolvedPartyQueryBuilder findByIdentificationType(Enterprise enterprise, IIdentificationType<?> idType)
	{
		return findByIdentificationType(enterprise, idType, null);
	}

	public InvolvedPartyQueryBuilder findByIdentificationType(Enterprise enterprise, IIdentificationType<?> idType, String value, UUID...identityTokens)
	{
		InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder joinTableQueryBuilder = new InvolvedPartyXInvolvedPartyIdentificationType().builder();
		InvolvedPartyIdentificationType type = GuiceContext.get(InvolvedPartyService.class).findIdentificationType(idType,enterprise,identityTokens);

		joinTableQueryBuilder.where(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, Equals, type);
		if (value != null)
		{
			joinTableQueryBuilder.withValue(value);
		}
		joinTableQueryBuilder.inDateRange();
		joinTableQueryBuilder.inActiveRange(enterprise,identityTokens);

		join(InvolvedParty_.identities, joinTableQueryBuilder, JoinType.INNER);

		inActiveRange(enterprise);
		inDateRange();

		return this;
	}
}
