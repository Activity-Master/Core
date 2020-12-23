package com.guicedee.activitymaster.core.db.entities.address.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.address.AddressXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.address.AddressXClassificationSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class AddressXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<AddressXClassificationSecurityTokenQueryBuilder, AddressXClassificationSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return AddressXClassificationSecurityToken_.base;
	}
}
