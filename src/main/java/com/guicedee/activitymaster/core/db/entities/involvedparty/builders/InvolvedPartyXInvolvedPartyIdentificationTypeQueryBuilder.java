package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.entityassist.enumerations.Operand;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.client.services.IInvolvedPartyService;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<InvolvedParty,
		InvolvedPartyIdentificationType,
		InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder,
		InvolvedPartyXInvolvedPartyIdentificationType,
		java.util.UUID>
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
	public boolean onCreate(InvolvedPartyXInvolvedPartyIdentificationType entity)
	{
		if (!Strings.isNullOrEmpty(entity.getValue()))
		{
			//entity.setValue(new Passwords().integerEncrypt(entity.getValue().getBytes()));
		}
		return super.onCreate(entity);
	}
	
	@Override
	public @jakarta.validation.constraints.NotNull InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder withValue(Operand operand, String value)
	{
		//String storeValue = new Passwords().integerEncrypt(value.getBytes());
		String storeValue = value;
		where(InvolvedPartyXInvolvedPartyIdentificationType_.value, operand, storeValue);
		return this;
	}
	
	@Override
	public InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder withType(String typeValue, ISystems<?,?> system, UUID... identityToken)
	{
		if (typeValue != null)
		{
			IInvolvedPartyService<?> service = GuiceContext.get(IInvolvedPartyService.class);
			InvolvedPartyIdentificationType at = (InvolvedPartyIdentificationType) service.findInvolvedPartyIdentificationType(typeValue, system, identityToken);
			where(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, Operand.Equals, at);
		}
		return this;
	}
}
