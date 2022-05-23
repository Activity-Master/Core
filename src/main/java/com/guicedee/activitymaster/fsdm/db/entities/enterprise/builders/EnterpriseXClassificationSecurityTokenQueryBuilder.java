package com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.EnterpriseXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.EnterpriseXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class EnterpriseXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EnterpriseXClassificationSecurityTokenQueryBuilder, EnterpriseXClassificationSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EnterpriseXClassificationSecurityToken_.base;
	}
}
