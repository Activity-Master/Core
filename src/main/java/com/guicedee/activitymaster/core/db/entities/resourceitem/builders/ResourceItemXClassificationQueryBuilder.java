package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXClassification;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXClassification_;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class ResourceItemXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<ResourceItem, Classification, ResourceItemXClassificationQueryBuilder,
						                                              ResourceItemXClassification, java.util.UUID, ResourceItemXClassificationSecurityToken>
{
	@Override
	public SingularAttribute<ResourceItemXClassification, ResourceItem> getPrimaryAttribute()
	{
		return ResourceItemXClassification_.resourceItemID;
	}

	@Override
	public  SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return ResourceItemXClassification_.classificationID;
	}
}
