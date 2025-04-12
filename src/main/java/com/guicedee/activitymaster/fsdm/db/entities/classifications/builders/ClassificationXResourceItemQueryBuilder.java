package com.guicedee.activitymaster.fsdm.db.entities.classifications.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.*;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class ClassificationXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Classification, ResourceItem, ClassificationXResourceItemQueryBuilder,
		ClassificationXResourceItem, UUID,ClassificationXResourceItemSecurityTokenQueryBuilder>
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
