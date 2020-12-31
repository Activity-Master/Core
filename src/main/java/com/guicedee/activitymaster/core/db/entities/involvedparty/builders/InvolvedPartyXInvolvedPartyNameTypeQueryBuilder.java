package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.INameType;
import com.guicedee.activitymaster.core.services.system.IInvolvedPartyService;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class InvolvedPartyXInvolvedPartyNameTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<InvolvedParty,
		InvolvedPartyNameType,
		InvolvedPartyXInvolvedPartyNameTypeQueryBuilder,
		InvolvedPartyXInvolvedPartyNameType,
		INameType<?>,
		java.util.UUID,
		InvolvedPartyXInvolvedPartyNameTypeSecurityToken>
{
	@Override
	public SingularAttribute<InvolvedPartyXInvolvedPartyNameType, InvolvedParty> getPrimaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyNameType_.involvedPartyID;
	}
	
	@Override
	public  SingularAttribute<InvolvedPartyXInvolvedPartyNameType, InvolvedPartyNameType>  getSecondaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyNameType_.involvedPartyNameTypeID;
	}
	
	@Override
	public InvolvedPartyXInvolvedPartyNameTypeQueryBuilder withType(String typeValue, ISystems<?> system, UUID... identityToken)
	{
		if (typeValue != null)
		{
			IInvolvedPartyService<?> service = GuiceContext.get(IInvolvedPartyService.class);
			InvolvedPartyNameType at = (InvolvedPartyNameType) service.findNameType(typeValue, system.getEnterprise(), identityToken);
			where(InvolvedPartyXInvolvedPartyNameType_.involvedPartyNameTypeID, Operand.Equals, at);
		}
		return this;
	}
}
