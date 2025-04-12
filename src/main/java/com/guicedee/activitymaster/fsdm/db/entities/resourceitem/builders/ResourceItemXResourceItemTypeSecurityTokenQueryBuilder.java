package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemXResourceItemTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemXResourceItemTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class ResourceItemXResourceItemTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemXResourceItemTypeSecurityTokenQueryBuilder, ResourceItemXResourceItemTypeSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemXResourceItemTypeSecurityToken_.base;
	}
}
