package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class InvolvedPartyXInvolvedPartyTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<InvolvedParty, InvolvedPartyType,
		InvolvedPartyXInvolvedPartyTypeQueryBuilder,
		InvolvedPartyXInvolvedPartyType,
		UUID,
		InvolvedPartyXInvolvedPartyTypeSecurityTokenQueryBuilder
		>
{
	@Override
	public SingularAttribute<InvolvedPartyXInvolvedPartyType, InvolvedParty> getPrimaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyType_.involvedPartyID;
	}
	
	@Override
	public SingularAttribute<InvolvedPartyXInvolvedPartyType, InvolvedPartyType> getSecondaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyType_.involvedPartyTypeID;
	}
	
	@Override
	public InvolvedPartyXInvolvedPartyTypeQueryBuilder withType(String typeValue, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (typeValue != null)
		{
			JoinExpression<?, ?, ?> joinExpression = new JoinExpression<>();
			join(getAttribute(InvolvedPartyXInvolvedPartyType_.INVOLVED_PARTY_TYPE_ID), JoinType.INNER, joinExpression);
			var nameFilter = joinExpression.getFilter(InvolvedPartyType_.NAME, Equals, typeValue);
			getFilters().add(nameFilter);
		}
		return this;
	}
}
