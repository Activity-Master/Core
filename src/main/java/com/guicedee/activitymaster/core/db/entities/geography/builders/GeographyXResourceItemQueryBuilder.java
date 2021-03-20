package com.guicedee.activitymaster.core.db.entities.geography.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.geography.*;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.metamodel.SingularAttribute;

public class GeographyXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Geography, ResourceItem, GeographyXResourceItemQueryBuilder,
				                                              GeographyXResourceItem, java.util.UUID>
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
