package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXProduct;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXProductSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXProduct_;
import com.armineasy.activitymaster.activitymaster.db.entities.product.Product;

import javax.persistence.metamodel.Attribute;

public class InvolvedPartyXProductQueryBuilder
		extends QueryBuilderRelationshipClassification<InvolvedParty, Product, InvolvedPartyXProductQueryBuilder,
				                                              InvolvedPartyXProduct, Long, InvolvedPartyXProductSecurityToken>
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
