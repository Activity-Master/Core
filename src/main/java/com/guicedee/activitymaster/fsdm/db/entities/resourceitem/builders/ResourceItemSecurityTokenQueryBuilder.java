package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class ResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemSecurityTokenQueryBuilder, ResourceItemSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemSecurityToken_.base;
	}
}
