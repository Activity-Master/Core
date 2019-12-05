package com.guicedee.activitymaster.core.db.entities.security.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokenXSecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokenXSecurityTokenSecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokenXSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class SecurityTokenXSecurityTokenQueryBuilder
		extends QueryBuilderRelationshipClassification<SecurityToken, SecurityToken, SecurityTokenXSecurityTokenQueryBuilder,
				                                              SecurityTokenXSecurityToken, Long, SecurityTokenXSecurityTokenSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return SecurityTokenXSecurityToken_.parentSecurityTokenID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return SecurityTokenXSecurityToken_.childSecurityTokenID;
	}
}
