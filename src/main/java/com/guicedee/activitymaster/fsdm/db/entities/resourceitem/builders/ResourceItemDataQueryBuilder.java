package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceDataQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemData;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemData_;

import java.util.UUID;

public class ResourceItemDataQueryBuilder
		extends QueryBuilderSCD<ResourceItemDataQueryBuilder, ResourceItemData, UUID,ResourceItemDataSecurityTokenQueryBuilder>
		implements IResourceDataQueryBuilder<ResourceItemDataQueryBuilder, ResourceItemData>
{
	public ResourceItemDataQueryBuilder withResource(ResourceItem resourceItem)
	{
		where(ResourceItemData_.resource, Operand.Equals, resourceItem);
		return this;
	}
}
