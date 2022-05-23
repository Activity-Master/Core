package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.fsdm.client.services.IProductService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductType;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.metamodel.SingularAttribute;



public class InvolvedPartyXProductTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<InvolvedParty, ProductType,
		InvolvedPartyXProductTypeQueryBuilder,
		InvolvedPartyXProductType,
		java.lang.String>
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
	public InvolvedPartyXProductTypeQueryBuilder withType(String typeValue, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (typeValue != null)
		{
			IProductService<?> service = GuiceContext.get(IProductService.class);
			ProductType at = (ProductType) service.findProductTypeForProduct(typeValue, system, identityToken);
			where(InvolvedPartyXProductType_.involvedPartyTypeID, Operand.Equals, at);
		}
		return this;
	}
}
