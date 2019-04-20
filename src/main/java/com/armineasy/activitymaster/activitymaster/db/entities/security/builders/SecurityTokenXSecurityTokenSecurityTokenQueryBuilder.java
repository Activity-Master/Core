package com.armineasy.activitymaster.activitymaster.db.entities.security.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXSecurityTokenSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXSecurityTokenSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class SecurityTokenXSecurityTokenSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<SecurityTokenXSecurityTokenSecurityTokenQueryBuilder, SecurityTokenXSecurityTokenSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return SecurityTokenXSecurityTokenSecurityToken_.base;
	}
}
