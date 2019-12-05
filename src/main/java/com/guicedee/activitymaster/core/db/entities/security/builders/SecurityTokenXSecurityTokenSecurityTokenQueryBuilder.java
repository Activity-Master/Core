package com.guicedee.activitymaster.core.db.entities.security.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokenXSecurityTokenSecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokenXSecurityTokenSecurityToken_;

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
