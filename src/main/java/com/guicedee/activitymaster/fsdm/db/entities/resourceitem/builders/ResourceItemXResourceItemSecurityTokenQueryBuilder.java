package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemXResourceItemSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemXResourceItemSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class ResourceItemXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemXResourceItemSecurityTokenQueryBuilder, ResourceItemXResourceItemSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemXResourceItemSecurityToken_.base;
	}
}
