package com.armineasy.activitymaster.activitymaster.db.entities.geography.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.Geography;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXResourceItem_;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;

import javax.persistence.metamodel.Attribute;

public class GeographyXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Geography, ResourceItem, GeographyXResourceItemQueryBuilder,
				                                              GeographyXResourceItem, Long, GeographyXResourceItemSecurityToken>
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
