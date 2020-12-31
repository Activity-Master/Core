package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXProduct;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXProductSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXProduct_;
import com.guicedee.activitymaster.core.db.entities.product.Product;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class ArrangementXProductQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, Product, ArrangementXProductQueryBuilder,
						                                              ArrangementXProduct, java.util.UUID, ArrangementXProductSecurityToken>
{
	@Override
	public SingularAttribute<ArrangementXProduct, Arrangement> getPrimaryAttribute()
	{
		return ArrangementXProduct_.arrangementID;
	}

	@Override
	public SingularAttribute<ArrangementXProduct, Product> getSecondaryAttribute()
	{
		return ArrangementXProduct_.productID;
	}
}
