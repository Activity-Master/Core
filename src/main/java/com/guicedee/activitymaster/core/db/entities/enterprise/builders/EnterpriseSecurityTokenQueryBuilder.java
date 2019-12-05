package com.guicedee.activitymaster.core.db.entities.enterprise.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.enterprise.EnterpriseSecurityToken;
import com.guicedee.activitymaster.core.db.entities.enterprise.EnterpriseSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class EnterpriseSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EnterpriseSecurityTokenQueryBuilder, EnterpriseSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EnterpriseSecurityToken_.base;
	}
}
