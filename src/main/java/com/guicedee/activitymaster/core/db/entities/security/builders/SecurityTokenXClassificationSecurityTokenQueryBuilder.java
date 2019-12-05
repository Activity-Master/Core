package com.guicedee.activitymaster.core.db.entities.security.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokenXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokenXClassificationSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class SecurityTokenXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<SecurityTokenXClassificationSecurityTokenQueryBuilder, SecurityTokenXClassificationSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return SecurityTokenXClassificationSecurityToken_.base;
	}
}
