package com.guicedee.activitymaster.fsdm.db.entities.address.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.address.AddressXResourceItemSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.address.AddressXResourceItemSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class AddressXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<AddressXResourceItemSecurityTokenQueryBuilder, AddressXResourceItemSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return AddressXResourceItemSecurityToken_.base;
	}
}
