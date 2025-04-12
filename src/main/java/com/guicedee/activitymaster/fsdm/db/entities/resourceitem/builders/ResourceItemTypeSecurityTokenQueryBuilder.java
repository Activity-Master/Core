package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class ResourceItemTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemTypeSecurityTokenQueryBuilder, ResourceItemTypeSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemTypeSecurityToken_.base;
	}
}
