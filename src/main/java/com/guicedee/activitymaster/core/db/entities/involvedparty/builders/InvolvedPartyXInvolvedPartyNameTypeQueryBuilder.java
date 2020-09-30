package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.enumtypes.INameType;
import com.guicedee.activitymaster.core.services.system.IInvolvedPartyService;
import com.guicedee.guicedinjection.GuiceContext;

import javax.persistence.metamodel.Attribute;
import java.util.UUID;

public class InvolvedPartyXInvolvedPartyNameTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<InvolvedParty,
		InvolvedPartyNameType,
		InvolvedPartyXInvolvedPartyNameTypeQueryBuilder,
		InvolvedPartyXInvolvedPartyNameType,
		INameType<?>,
		Long,
		InvolvedPartyXInvolvedPartyNameTypeSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyNameType_.involvedPartyID;
	}
	
	@Override
	public Attribute getSecondaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyNameType_.involvedPartyNameTypeID;
	}
	
	@Override
	public InvolvedPartyXInvolvedPartyNameTypeQueryBuilder withType(String typeValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (typeValue != null)
		{
			IInvolvedPartyService<?> service = GuiceContext.get(IInvolvedPartyService.class);
			InvolvedPartyNameType at = (InvolvedPartyNameType) service.findNameType(typeValue, enterprise, identityToken);
			where(InvolvedPartyXInvolvedPartyNameType_.involvedPartyNameTypeID, Operand.Equals, at);
		}
		return this;
	}
}
