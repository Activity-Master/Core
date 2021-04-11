package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.fsdm.client.services.IArrangementsService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.*;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class ArrangementXArrangementTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<Arrangement,
		ArrangementType,
		ArrangementXArrangementTypeQueryBuilder,
		ArrangementXArrangementType,
		UUID>
{
	@Override
	public  SingularAttribute<ArrangementXArrangementType, Arrangement> getPrimaryAttribute()
	{
		return ArrangementXArrangementType_.arrangement;
	}
	
	@Override
	public SingularAttribute<ArrangementXArrangementType, ArrangementType> getSecondaryAttribute()
	{
		return ArrangementXArrangementType_.type;
	}
	
	@Override
	public ArrangementXArrangementTypeQueryBuilder withType(String typeValue, ISystems<?,?> system, UUID... identityToken)
	{
		if (typeValue != null)
		{
			IArrangementsService<?> service = GuiceContext.get(IArrangementsService.class);
			ArrangementType at = (ArrangementType) service.find(typeValue, system, identityToken);
			where(ArrangementXArrangementType_.type, Operand.Equals, at);
		}
		return this;
	}
}
