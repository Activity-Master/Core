package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IContainsClassificationsQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXClassification;

public class ResourceItemQueryBuilder
		extends QueryBuilderTable<ResourceItemQueryBuilder, ResourceItem, java.util.UUID, ResourceItemSecurityToken>
		implements IContainsClassificationsQueryBuilder<ResourceItemQueryBuilder, ResourceItem, java.util.UUID, ResourceItemXClassification>
{

}
