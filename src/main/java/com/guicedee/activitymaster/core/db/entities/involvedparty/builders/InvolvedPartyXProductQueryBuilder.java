package com.guicedee.activitymaster.core.db.entities.involvedparty.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXProduct;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXProductSecurityToken;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXProduct_;
import com.guicedee.activitymaster.core.db.entities.product.Product;

import jakarta.persistence.metamodel.Attribute;

public class InvolvedPartyXProductQueryBuilder
		extends QueryBuilderRelationshipClassification<InvolvedParty, Product, InvolvedPartyXProductQueryBuilder,
						                                              InvolvedPartyXProduct, java.util.UUID, InvolvedPartyXProductSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return InvolvedPartyXProduct_.involvedPartyID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return InvolvedPartyXProduct_.productID;
	}
}
