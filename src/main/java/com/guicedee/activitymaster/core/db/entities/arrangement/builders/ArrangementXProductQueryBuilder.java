package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXProduct;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXProductSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXProduct_;
import com.guicedee.activitymaster.core.db.entities.product.Product;

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
