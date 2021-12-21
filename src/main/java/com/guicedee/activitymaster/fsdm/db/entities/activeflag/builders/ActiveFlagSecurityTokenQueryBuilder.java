package com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlagSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlagSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ActiveFlagSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ActiveFlagSecurityTokenQueryBuilder, ActiveFlagSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ActiveFlagSecurityToken_.base;
	}
	
}
