package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import jakarta.persistence.metamodel.SingularAttribute;



public class InvolvedPartyXInvolvedPartyNameTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<InvolvedParty,
		InvolvedPartyNameType,
		InvolvedPartyXInvolvedPartyNameTypeQueryBuilder,
		InvolvedPartyXInvolvedPartyNameType,
		java.lang.String>
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
			IInvolvedPartyService<?> service = com.guicedee.client.IGuiceContext.get(IInvolvedPartyService.class);
			InvolvedPartyNameType at = (InvolvedPartyNameType) service.findInvolvedPartyNameType(typeValue, system, identityToken);
			where(InvolvedPartyXInvolvedPartyNameType_.involvedPartyNameTypeID, Operand.Equals, at);
		}
		return this;
	}
}
