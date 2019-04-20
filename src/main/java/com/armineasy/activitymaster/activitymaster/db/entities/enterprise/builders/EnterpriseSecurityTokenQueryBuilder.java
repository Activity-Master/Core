package com.armineasy.activitymaster.activitymaster.db.entities.enterprise.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.EnterpriseSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.EnterpriseSecurityToken_;

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
