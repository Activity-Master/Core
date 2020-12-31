package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.activitymaster.core.db.entities.product.ProductType;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IProductTypeValue;
import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;
import com.guicedee.activitymaster.core.services.system.IInvolvedPartyService;
import com.guicedee.activitymaster.core.services.system.IProductService;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class InvolvedPartyXProductTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<InvolvedParty, ProductType,
		InvolvedPartyXProductTypeQueryBuilder,
		InvolvedPartyXProductType,
		IProductTypeValue<?>,
		UUID,
		InvolvedPartyXProductTypeSecurityToken>
{
	@Override
	public SingularAttribute<InvolvedPartyXProductType, InvolvedParty> getPrimaryAttribute()
	{
		return InvolvedPartyXProductType_.involvedPartyID;
	}
	
	@Override
	public SingularAttribute<InvolvedPartyXProductType, ProductType> getSecondaryAttribute()
	{
		return InvolvedPartyXProductType_.involvedPartyTypeID;
	}
	
	@Override
	public InvolvedPartyXProductTypeQueryBuilder withType(String typeValue, ISystems<?> system, UUID... identityToken)
	{
		if (typeValue != null)
		{
			IProductService<?> service = GuiceContext.get(IProductService.class);
			ProductType at = (ProductType) service.findProductType(typeValue, system, identityToken);
			where(InvolvedPartyXProductType_.involvedPartyTypeID, Operand.Equals, at);
		}
		return this;
	}
}
