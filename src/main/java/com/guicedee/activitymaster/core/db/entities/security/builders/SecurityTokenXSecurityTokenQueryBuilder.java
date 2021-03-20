package com.guicedee.activitymaster.core.db.entities.security.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.security.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class SecurityTokenXSecurityTokenQueryBuilder
		extends QueryBuilderRelationshipClassification<SecurityToken, SecurityToken, SecurityTokenXSecurityTokenQueryBuilder,
				                                              SecurityTokenXSecurityToken, java.util.UUID>
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
