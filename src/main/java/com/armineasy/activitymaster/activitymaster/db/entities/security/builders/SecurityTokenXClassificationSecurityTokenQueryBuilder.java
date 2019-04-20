package com.armineasy.activitymaster.activitymaster.db.entities.security.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXClassificationSecurityToken_;

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
