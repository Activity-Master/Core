package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ResourceItemXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ResourceItemXClassificationSecurityTokenQueryBuilder, ResourceItemXClassificationSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ResourceItemXClassificationSecurityToken_.base;
	}
}
