package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.entityassist.enumerations.Operand;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.fsdm.api.Passwords;
import com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;


public class InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<InvolvedParty,
		InvolvedPartyIdentificationType,
		InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder,
		InvolvedPartyXInvolvedPartyIdentificationType,
		UUID,
		InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<InvolvedPartyXInvolvedPartyIdentificationType, InvolvedParty> getPrimaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyID;
	}
	
	@Override
	public SingularAttribute<InvolvedPartyXInvolvedPartyIdentificationType, InvolvedPartyIdentificationType> getSecondaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID;
	}
	
	@Override
	public  InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder withValue(Operand operand, String value)
	{
		if (Strings.isNullOrEmpty(value))
		{
			return this;
		}
		String storeValue = value;
		if ("true".equals(System.getProperty("encrypt", "true")))
		{
			storeValue = new Passwords().integerEncrypt(value.getBytes());
		}
		where(InvolvedPartyXInvolvedPartyIdentificationType_.value, operand, storeValue);
		return this;
	}
	
	@Override
	public InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder withType(String typeValue, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (typeValue != null)
		{
			IInvolvedPartyService<?> service = com.guicedee.client.IGuiceContext.get(IInvolvedPartyService.class);
			InvolvedPartyIdentificationType at = (InvolvedPartyIdentificationType) service.findInvolvedPartyIdentificationType(typeValue, system, identityToken);
			where(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, Operand.Equals, at);
			inActiveRange();
			inDateRange();
		}
		return this;
	}
}
