package com.guicedee.activitymaster.fsdm.db.entities.classifications.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.*;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.metamodel.SingularAttribute;

public class ClassificationXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Classification, ResourceItem, ClassificationXResourceItemQueryBuilder,
		ClassificationXResourceItem, java.util.UUID>
{
	@Override
	public SingularAttribute<ClassificationXResourceItem, Classification> getPrimaryAttribute()
	{
		return ClassificationXResourceItem_.classificationID;
	}
	
	@Override
	public SingularAttribute<ClassificationXResourceItem, ResourceItem> getSecondaryAttribute()
	{
		return ClassificationXResourceItem_.resourceItemID;
	}
}
