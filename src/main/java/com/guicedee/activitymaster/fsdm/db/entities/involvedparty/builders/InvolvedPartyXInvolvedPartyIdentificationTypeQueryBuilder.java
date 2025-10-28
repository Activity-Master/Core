package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.entityassist.enumerations.Operand;
import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.fsdm.api.Passwords;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<InvolvedParty,
		InvolvedPartyIdentificationType,
		InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder,
		InvolvedPartyXInvolvedPartyIdentificationType,
		UUID,
		InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<InvolvedPartyXInvolvedPartyIdentificationType, InvolvedParty> getPrimaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyID;
	}
	
	@Override
	public SingularAttribute<InvolvedPartyXInvolvedPartyIdentificationType, InvolvedPartyIdentificationType> getSecondaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID;
	}
	
	@Override
	public  InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder withValue(Operand operand, String value)
	{
		if (Strings.isNullOrEmpty(value))
		{
			return this;
		}
		String storeValue = value;
		if ("true".equals(System.getProperty("encrypt", "true")))
		{
			storeValue = new Passwords().integerEncrypt(value.getBytes());
		}
		where(InvolvedPartyXInvolvedPartyIdentificationType_.value, operand, storeValue);
		return this;
	}
	
	@Override
	public InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder withType(String typeValue, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (typeValue != null)
		{
			JoinExpression<?, ?, ?> joinExpression = new JoinExpression<>();
			join(getAttribute(InvolvedPartyXInvolvedPartyIdentificationType_.INVOLVED_PARTY_IDENTIFICATION_TYPE_ID), JoinType.INNER, joinExpression);
			var nameFilter = joinExpression.getFilter(InvolvedPartyIdentificationType_.NAME, Equals, typeValue);
			getFilters().add(nameFilter);
			inActiveRange();
			inDateRange();
		}
		return this;
	}
}
