package com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlagSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlagSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class ActiveFlagSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ActiveFlagSecurityTokenQueryBuilder, ActiveFlagSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ActiveFlagSecurityToken_.base;
	}
	
}
