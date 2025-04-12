package com.guicedee.activitymaster.fsdm.db.entities.address.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.address.AddressXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.address.AddressXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class AddressXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<AddressXClassificationSecurityTokenQueryBuilder, AddressXClassificationSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return AddressXClassificationSecurityToken_.base;
	}
}
