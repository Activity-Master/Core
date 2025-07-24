package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;


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
			IInvolvedPartyService<?> service = com.guicedee.client.IGuiceContext.get(IInvolvedPartyService.class);
			InvolvedPartyType at = (InvolvedPartyType) service.findType(getEntityManager(), typeValue, system, identityToken);
			where(InvolvedPartyXInvolvedPartyType_.involvedPartyTypeID, Operand.Equals, at);
		}
		return this;
	}
}
