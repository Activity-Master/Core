package com.guicedee.activitymaster.core.db.entities.address.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.address.AddressXResourceItem;
import com.guicedee.activitymaster.core.db.entities.address.AddressXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.address.AddressXResourceItem_;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;

import jakarta.persistence.metamodel.Attribute;

public class AddressXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Address, ResourceItem, AddressXResourceItemQueryBuilder,
				                                              AddressXResourceItem, java.util.UUID, AddressXResourceItemSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return AddressXResourceItem_.addressID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return AddressXResourceItem_.resourceItemID;
	}
}
