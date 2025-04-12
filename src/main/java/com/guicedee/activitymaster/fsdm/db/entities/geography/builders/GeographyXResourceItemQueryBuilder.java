package com.guicedee.activitymaster.fsdm.db.entities.geography.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.geography.*;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class GeographyXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Geography, ResourceItem, GeographyXResourceItemQueryBuilder,
		GeographyXResourceItem, UUID,GeographyXResourceItemSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<GeographyXResourceItem, Geography> getPrimaryAttribute()
	{
		return GeographyXResourceItem_.geographyID;
	}
	
	@Override
	public SingularAttribute<GeographyXResourceItem, ResourceItem> getSecondaryAttribute()
	{
		return GeographyXResourceItem_.resourceItemID;
	}
}
