package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IContainsClassificationsQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXClassification;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem_;
import jakarta.validation.constraints.NotNull;

import static com.entityassist.enumerations.Operand.Equals;

public class ResourceItemQueryBuilder
		extends QueryBuilderTable<ResourceItemQueryBuilder, ResourceItem, java.util.UUID, ResourceItemSecurityToken>
		implements IContainsClassificationsQueryBuilder<ResourceItemQueryBuilder, ResourceItem, java.util.UUID, ResourceItemXClassification>
{
	@Override
	public @NotNull ResourceItemQueryBuilder withValue(String value)
	{
		where(ResourceItem_.resourceItemDataType, Equals, value);
		return this;
	}
}
