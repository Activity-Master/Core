package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class InvolvedPartyXInvolvedPartyNameTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<InvolvedParty,
		InvolvedPartyNameType,
		InvolvedPartyXInvolvedPartyNameTypeQueryBuilder,
		InvolvedPartyXInvolvedPartyNameType,
		UUID,
		InvolvedPartyXInvolvedPartyNameTypeSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<InvolvedPartyXInvolvedPartyNameType, InvolvedParty> getPrimaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyNameType_.involvedPartyID;
	}
	
	@Override
	public SingularAttribute<InvolvedPartyXInvolvedPartyNameType, InvolvedPartyNameType> getSecondaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyNameType_.involvedPartyNameTypeID;
	}
	
	@Override
	public InvolvedPartyXInvolvedPartyNameTypeQueryBuilder withType(String typeValue, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (typeValue != null)
		{
			JoinExpression<?, ?, ?> joinExpression = new JoinExpression<>();
			join(getAttribute(InvolvedPartyXInvolvedPartyNameType_.INVOLVED_PARTY_NAME_TYPE_ID), JoinType.INNER, joinExpression);
			var nameFilter = joinExpression.getFilter(InvolvedPartyNameType_.NAME, Equals, typeValue);
			getFilters().add(nameFilter);
		}
		return this;
	}
}
