package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;
import com.guicedee.activitymaster.core.services.system.IInvolvedPartyService;
import com.guicedee.guicedinjection.GuiceContext;

import javax.persistence.metamodel.Attribute;
import java.util.UUID;

public class InvolvedPartyXInvolvedPartyTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<InvolvedParty, InvolvedPartyType,
		InvolvedPartyXInvolvedPartyTypeQueryBuilder,
		InvolvedPartyXInvolvedPartyType,
		ITypeValue<?>,
		Long,
		InvolvedPartyXInvolvedPartyTypeSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyType_.involvedPartyID;
	}
	
	@Override
	public Attribute getSecondaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyType_.involvedPartyTypeID;
	}
	
	@Override
	public InvolvedPartyXInvolvedPartyTypeQueryBuilder withType(String typeValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (typeValue != null)
		{
			IInvolvedPartyService<?> service = GuiceContext.get(IInvolvedPartyService.class);
			InvolvedPartyType at = (InvolvedPartyType) service.findType(typeValue, enterprise, identityToken);
			where(InvolvedPartyXInvolvedPartyType_.involvedPartyTypeID, Operand.Equals, at);
		}
		return this;
	}
}
