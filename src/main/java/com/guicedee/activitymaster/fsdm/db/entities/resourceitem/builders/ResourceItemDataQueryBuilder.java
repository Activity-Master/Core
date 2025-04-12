package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceDataQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemData;

import java.util.UUID;

public class ResourceItemDataQueryBuilder
		extends QueryBuilderSCD<ResourceItemDataQueryBuilder, ResourceItemData, UUID,ResourceItemDataSecurityTokenQueryBuilder>
		implements IResourceDataQueryBuilder<ResourceItemDataQueryBuilder, ResourceItemData>
{
}
