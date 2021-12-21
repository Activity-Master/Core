package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemDataXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemDataXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ResourceItemDataXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemDataXClassificationSecurityTokenQueryBuilder, ResourceItemDataXClassificationSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemDataXClassificationSecurityToken_.base;
	}
}
