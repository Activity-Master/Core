package com.guicedee.activitymaster.core.db.entities.enterprise.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.enterprise.EnterpriseXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.enterprise.EnterpriseXClassificationSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class EnterpriseXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EnterpriseXClassificationSecurityTokenQueryBuilder, EnterpriseXClassificationSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EnterpriseXClassificationSecurityToken_.base;
	}
}
