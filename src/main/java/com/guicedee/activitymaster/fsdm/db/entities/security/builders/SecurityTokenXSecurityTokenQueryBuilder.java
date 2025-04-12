package com.guicedee.activitymaster.fsdm.db.entities.security.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.security.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class SecurityTokenXSecurityTokenQueryBuilder
		extends QueryBuilderRelationshipClassification<SecurityToken, SecurityToken, SecurityTokenXSecurityTokenQueryBuilder,
		SecurityTokenXSecurityToken, UUID,SecurityTokenXSecurityTokenSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<SecurityTokenXSecurityToken, SecurityToken> getPrimaryAttribute()
	{
		return SecurityTokenXSecurityToken_.parentSecurityTokenID;
	}
	
	@Override
	public SingularAttribute<SecurityTokenXSecurityToken, SecurityToken> getSecondaryAttribute()
	{
		return SecurityTokenXSecurityToken_.childSecurityTokenID;
	}
}
