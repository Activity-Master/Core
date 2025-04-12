package com.guicedee.activitymaster.fsdm.db.entities.security.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityTokenXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityTokenXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class SecurityTokenXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<SecurityTokenXClassificationSecurityTokenQueryBuilder, SecurityTokenXClassificationSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return SecurityTokenXClassificationSecurityToken_.base;
	}
}
