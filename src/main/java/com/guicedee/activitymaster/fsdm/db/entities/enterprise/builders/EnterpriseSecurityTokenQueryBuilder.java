package com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.EnterpriseSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.EnterpriseSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class EnterpriseSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EnterpriseSecurityTokenQueryBuilder, EnterpriseSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EnterpriseSecurityToken_.base;
	}
}
