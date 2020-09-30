package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IContainsClassificationsQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.*;

public class ResourceItemDataQueryBuilder
		extends QueryBuilderTable<ResourceItemDataQueryBuilder, ResourceItemData, Long, ResourceItemDataSecurityToken>
		implements IContainsClassificationsQueryBuilder<ResourceItemDataQueryBuilder, ResourceItemData, Long, ResourceItemDataXClassification>
{
}
