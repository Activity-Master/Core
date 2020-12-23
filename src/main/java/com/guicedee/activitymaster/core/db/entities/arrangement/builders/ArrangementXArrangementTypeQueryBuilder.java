package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.entities.arrangement.*;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.enumtypes.IArrangementTypes;
import com.guicedee.activitymaster.core.services.system.IArrangementsService;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.persistence.metamodel.Attribute;
import java.util.UUID;

public class ArrangementXArrangementTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<Arrangement,
		ArrangementType,
		ArrangementXArrangementTypeQueryBuilder,
		ArrangementXArrangementType,
		IArrangementTypes<?>,
		java.util.UUID,
		ArrangementXArrangementTypeSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ArrangementXArrangementType_.arrangement;
	}
	
	@Override
	public Attribute getSecondaryAttribute()
	{
		return ArrangementXArrangementType_.type;
	}
	
	@Override
	public ArrangementXArrangementTypeQueryBuilder withType(String typeValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (typeValue != null)
		{
			IArrangementsService<?> service = GuiceContext.get(IArrangementsService.class);
			ArrangementType at = (ArrangementType) service.find(typeValue, enterprise, identityToken);
			where(ArrangementXArrangementType_.type, Operand.Equals, at);
		}
		return this;
	}
}
