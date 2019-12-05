package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemDataXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemDataXClassificationSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ResourceItemDataXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemDataXClassificationSecurityTokenQueryBuilder, ResourceItemDataXClassificationSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemDataXClassificationSecurityToken_.base;
	}
}
