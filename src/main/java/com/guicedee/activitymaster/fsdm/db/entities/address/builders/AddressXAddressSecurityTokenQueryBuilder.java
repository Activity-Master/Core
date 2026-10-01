package com.guicedee.activitymaster.fsdm.db.entities.address.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.address.AddressXAddressSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.address.AddressXAddressSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class AddressXAddressSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<AddressXAddressSecurityTokenQueryBuilder, AddressXAddressSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return AddressXAddressSecurityToken_.base;
	}
}
