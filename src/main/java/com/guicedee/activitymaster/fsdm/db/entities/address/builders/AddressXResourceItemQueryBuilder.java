package com.guicedee.activitymaster.fsdm.db.entities.address.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.address.*;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.metamodel.SingularAttribute;

public class AddressXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Address, ResourceItem, AddressXResourceItemQueryBuilder,
		AddressXResourceItem, java.lang.String>
{
	@Override
	public SingularAttribute<AddressXResourceItem, Address> getPrimaryAttribute()
	{
		return AddressXResourceItem_.addressID;
	}
	
	@Override
	public SingularAttribute<AddressXResourceItem, ResourceItem> getSecondaryAttribute()
	{
		return AddressXResourceItem_.resourceItemID;
	}
}
