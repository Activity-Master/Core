package com.armineasy.activitymaster.activitymaster.db.entities.address.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXResourceItem_;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;

import javax.persistence.metamodel.Attribute;

public class AddressXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Address, ResourceItem, AddressXResourceItemQueryBuilder,
				                                              AddressXResourceItem, Long, AddressXResourceItemSecurityToken>
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
