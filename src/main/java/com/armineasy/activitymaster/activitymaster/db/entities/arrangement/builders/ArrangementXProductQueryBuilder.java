package com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.Arrangement;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXProduct;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXProductSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXProduct_;
import com.armineasy.activitymaster.activitymaster.db.entities.product.Product;

import javax.persistence.metamodel.Attribute;

public class ArrangementXProductQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, Product, ArrangementXProductQueryBuilder,
				                                              ArrangementXProduct, Long, ArrangementXProductSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ArrangementXProduct_.arrangementID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ArrangementXProduct_.productID;
	}
}
