package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.client.services.builders.warehouse.resourceitem.IResourceItemTypeQueryBuilder;
import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderSCDNameDescription;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemType;

public class ResourceItemTypeQueryBuilder
		extends QueryBuilderSCDNameDescription<ResourceItemTypeQueryBuilder, ResourceItemType, java.util.UUID>
		implements IResourceItemTypeQueryBuilder<ResourceItemTypeQueryBuilder,ResourceItemType>
{

}
