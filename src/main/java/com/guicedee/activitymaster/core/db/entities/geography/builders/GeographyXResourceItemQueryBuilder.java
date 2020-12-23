package com.guicedee.activitymaster.core.db.entities.geography.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXResourceItem;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXResourceItem_;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;

import jakarta.persistence.metamodel.Attribute;

public class GeographyXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Geography, ResourceItem, GeographyXResourceItemQueryBuilder,
				                                              GeographyXResourceItem, java.util.UUID, GeographyXResourceItemSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return GeographyXResourceItem_.geographyID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return GeographyXResourceItem_.resourceItemID;
	}
}
