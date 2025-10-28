package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductType;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class InvolvedPartyXProductTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<InvolvedParty, ProductType,
		InvolvedPartyXProductTypeQueryBuilder,
		InvolvedPartyXProductType,
		UUID,
		InvolvedPartyXProductTypeSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<InvolvedPartyXProductType, InvolvedParty> getPrimaryAttribute()
	{
		return InvolvedPartyXProductType_.involvedPartyID;
	}
	
	@Override
	public SingularAttribute<InvolvedPartyXProductType, ProductType> getSecondaryAttribute()
	{
		return InvolvedPartyXProductType_.involvedPartyTypeID;
	}
	
	@Override
	public InvolvedPartyXProductTypeQueryBuilder withType(String typeValue, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (typeValue != null)
		{
			JoinExpression<?, ?, ?> joinExpression = new JoinExpression<>();
			join(getAttribute(InvolvedPartyXProductType_.INVOLVED_PARTY_TYPE_ID), JoinType.INNER, joinExpression);
			var nameFilter = joinExpression.getFilter(com.guicedee.activitymaster.fsdm.db.entities.product.ProductType_.NAME, Equals, typeValue);
			getFilters().add(nameFilter);
		}
		return this;
	}
}
