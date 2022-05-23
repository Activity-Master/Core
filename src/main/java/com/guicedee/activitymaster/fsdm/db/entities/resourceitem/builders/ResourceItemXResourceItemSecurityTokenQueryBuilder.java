package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemXResourceItemSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemXResourceItemSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ResourceItemXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemXResourceItemSecurityTokenQueryBuilder, ResourceItemXResourceItemSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemXResourceItemSecurityToken_.base;
	}
}
