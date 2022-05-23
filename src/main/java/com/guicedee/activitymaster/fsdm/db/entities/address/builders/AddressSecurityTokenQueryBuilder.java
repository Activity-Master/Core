package com.guicedee.activitymaster.fsdm.db.entities.address.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.address.AddressSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.address.AddressSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class AddressSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<AddressSecurityTokenQueryBuilder, AddressSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return AddressSecurityToken_.base;
	}
}
