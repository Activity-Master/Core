package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXClassificationSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class ResourceItemXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemXClassificationSecurityTokenQueryBuilder, ResourceItemXClassificationSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemXClassificationSecurityToken_.base;
	}
}
