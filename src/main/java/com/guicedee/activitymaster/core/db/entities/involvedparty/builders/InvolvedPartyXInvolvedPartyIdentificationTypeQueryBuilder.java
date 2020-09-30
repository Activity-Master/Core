package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.enumtypes.IIdentificationType;
import com.guicedee.activitymaster.core.services.system.IInvolvedPartyService;
import com.guicedee.guicedinjection.GuiceContext;

import javax.persistence.metamodel.Attribute;
import java.util.UUID;

public class InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<InvolvedParty,
		InvolvedPartyIdentificationType,
		InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder,
		InvolvedPartyXInvolvedPartyIdentificationType,
		IIdentificationType<?>,
		Long,
		InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyID;
	}
	
	@Override
	public Attribute getSecondaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID;
	}
	
	@Override
	public InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder withType(String typeValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (typeValue != null)
		{
			IInvolvedPartyService<?> service = GuiceContext.get(IInvolvedPartyService.class);
			InvolvedPartyIdentificationType at = (InvolvedPartyIdentificationType) service.findIdentificationType(typeValue, enterprise, identityToken);
			where(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, Operand.Equals, at);
		}
		return this;
	}
}
