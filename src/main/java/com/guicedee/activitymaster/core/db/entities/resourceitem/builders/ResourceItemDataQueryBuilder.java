package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.client.services.builders.warehouse.resourceitem.IResourceDataQueryBuilder;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemData;

public class ResourceItemDataQueryBuilder
		extends QueryBuilderTable<ResourceItemDataQueryBuilder, ResourceItemData, java.util.UUID>
		implements IResourceDataQueryBuilder<ResourceItemDataQueryBuilder,ResourceItemData>
{
}
