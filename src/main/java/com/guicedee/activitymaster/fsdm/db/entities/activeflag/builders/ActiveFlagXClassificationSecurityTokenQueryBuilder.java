package com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlagXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlagXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ActiveFlagXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ActiveFlagXClassificationSecurityTokenQueryBuilder, ActiveFlagXClassificationSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ActiveFlagXClassificationSecurityToken_.base;
	}
}
