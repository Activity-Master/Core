package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.*;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class ArrangementXProductQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, Product, ArrangementXProductQueryBuilder,
		ArrangementXProduct, UUID,ArrangementXProductSecurityTokenQueryBuilder>
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
