package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class InvolvedPartyXProductQueryBuilder
		extends QueryBuilderRelationshipClassification<InvolvedParty, Product, InvolvedPartyXProductQueryBuilder,
		InvolvedPartyXProduct, UUID,InvolvedPartyXProductSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<InvolvedPartyXProduct, InvolvedParty> getPrimaryAttribute()
	{
		return InvolvedPartyXProduct_.involvedPartyID;
	}
	
	@Override
	public SingularAttribute<InvolvedPartyXProduct, Product> getSecondaryAttribute()
	{
		return InvolvedPartyXProduct_.productID;
	}
}
