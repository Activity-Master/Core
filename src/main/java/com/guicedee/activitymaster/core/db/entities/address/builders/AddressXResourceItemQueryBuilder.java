package com.guicedee.activitymaster.core.db.entities.address.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.address.AddressXResourceItem;
import com.guicedee.activitymaster.core.db.entities.address.AddressXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.address.AddressXResourceItem_;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class AddressXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Address, ResourceItem, AddressXResourceItemQueryBuilder,
				                                              AddressXResourceItem, java.util.UUID, AddressXResourceItemSecurityToken>
{
	@Override
	public SingularAttribute<AddressXResourceItem, Address> getPrimaryAttribute()
	{
		return AddressXResourceItem_.addressID;
	}

	@Override
	public  SingularAttribute<AddressXResourceItem, ResourceItem> getSecondaryAttribute()
	{
		return AddressXResourceItem_.resourceItemID;
	}
}
