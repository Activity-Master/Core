package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemXResourceItemTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemXResourceItemTypeSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class ResourceItemXResourceItemTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemXResourceItemTypeSecurityTokenQueryBuilder, ResourceItemXResourceItemTypeSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemXResourceItemTypeSecurityToken_.base;
	}
}
