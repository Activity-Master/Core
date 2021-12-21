package com.guicedee.activitymaster.fsdm.db.entities.security.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityTokenXSecurityTokenSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityTokenXSecurityTokenSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class SecurityTokenXSecurityTokenSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<SecurityTokenXSecurityTokenSecurityTokenQueryBuilder, SecurityTokenXSecurityTokenSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return SecurityTokenXSecurityTokenSecurityToken_.base;
	}
}
