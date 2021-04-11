package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceDataQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemData;

public class ResourceItemDataQueryBuilder
		extends QueryBuilderTable<ResourceItemDataQueryBuilder, ResourceItemData, java.util.UUID>
		implements IResourceDataQueryBuilder<ResourceItemDataQueryBuilder,ResourceItemData>
{
}
