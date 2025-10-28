package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.*;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class ArrangementXArrangementTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<Arrangement,
		ArrangementType,
		ArrangementXArrangementTypeQueryBuilder,
		ArrangementXArrangementType,
		UUID,
		ArrangementXArrangementTypeSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<ArrangementXArrangementType, Arrangement> getPrimaryAttribute()
	{
		return ArrangementXArrangementType_.arrangement;
	}
	
	@Override
	public SingularAttribute<ArrangementXArrangementType, ArrangementType> getSecondaryAttribute()
	{
		return ArrangementXArrangementType_.type;
	}
	
	@Override
	public ArrangementXArrangementTypeQueryBuilder withType(String typeValue, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (typeValue != null)
		{
			JoinExpression<?, ?, ?> joinExpression = new JoinExpression<>();
			join(getAttribute(ArrangementXArrangementType_.TYPE), JoinType.INNER, joinExpression);
			var nameFilter = joinExpression.getFilter(ArrangementType_.NAME, Equals, typeValue);
			getFilters().add(nameFilter);
		}
		return this;
	}
}
