package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.rules.*;
import jakarta.persistence.metamodel.Attribute;

public class RulesXProductQueryBuilder
		extends QueryBuilderRelationshipClassification<Rules, Product, RulesXProductQueryBuilder,
		RulesXProduct, java.util.UUID, RulesXProductSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return RulesXProduct_.rulesID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return RulesXProduct_.productID;
	}
}
