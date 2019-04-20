package com.armineasy.activitymaster.activitymaster.db.entities.security.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXSecurityTokenSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXSecurityToken_;

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
