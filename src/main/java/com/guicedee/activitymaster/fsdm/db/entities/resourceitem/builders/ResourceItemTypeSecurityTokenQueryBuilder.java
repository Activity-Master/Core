package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ResourceItemTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemTypeSecurityTokenQueryBuilder, ResourceItemTypeSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemTypeSecurityToken_.base;
	}
}
