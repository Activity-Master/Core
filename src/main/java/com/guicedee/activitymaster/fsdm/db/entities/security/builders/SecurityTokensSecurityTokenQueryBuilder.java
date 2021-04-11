package com.guicedee.activitymaster.fsdm.db.entities.security.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderEnterprise;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.security.*;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class SecurityTokensSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<SecurityTokensSecurityTokenQueryBuilder, SecurityTokensSecurityToken, UUID>
		implements IQueryBuilderEnterprise<SecurityTokensSecurityTokenQueryBuilder,SecurityTokensSecurityToken, UUID>
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
