package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItemTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemType;

import java.util.UUID;

public class ResourceItemTypeQueryBuilder
		extends QueryBuilderSCD<ResourceItemTypeQueryBuilder, ResourceItemType, UUID,ResourceItemTypeSecurityTokenQueryBuilder>
		implements IResourceItemTypeQueryBuilder<ResourceItemTypeQueryBuilder, ResourceItemType>,
		           IQueryBuilderNamesAndDescriptions<ResourceItemTypeQueryBuilder,ResourceItemType,java.util.UUID>
{

}
