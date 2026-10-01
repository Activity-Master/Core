package com.guicedee.activitymaster.fsdm.db.entities.address.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.address.AddressTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.address.AddressTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class AddressTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<AddressTypeSecurityTokenQueryBuilder, AddressTypeSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return AddressTypeSecurityToken_.base;
	}
}
