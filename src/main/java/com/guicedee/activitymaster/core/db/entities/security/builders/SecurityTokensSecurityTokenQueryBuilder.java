package com.guicedee.activitymaster.core.db.entities.security.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokensSecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokensSecurityToken_;

import javax.persistence.metamodel.Attribute;

import static com.entityassist.enumerations.Operand.*;

public class SecurityTokensSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<SecurityTokensSecurityTokenQueryBuilder, SecurityTokensSecurityToken, Long>
{
	public SecurityTokensSecurityTokenQueryBuilder findBySecurityToken(SecurityToken fromToken, SecurityToken forToken)
	{
		where(SecurityTokensSecurityToken_.securityTokenID, Equals, fromToken);
		where(SecurityTokensSecurityToken_.base, Equals, forToken);
		return this;
	}

	@Override
	protected Attribute getMyAttribute()
	{
		return SecurityTokensSecurityToken_.base;
	}
}
