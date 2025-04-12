package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemDataSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemDataSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class ResourceItemDataSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemDataSecurityTokenQueryBuilder, ResourceItemDataSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemDataSecurityToken_.base;
	}
}
