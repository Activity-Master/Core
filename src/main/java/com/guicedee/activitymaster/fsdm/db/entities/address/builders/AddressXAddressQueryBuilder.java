package com.guicedee.activitymaster.fsdm.db.entities.address.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.address.*;

import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class AddressXAddressQueryBuilder
		extends QueryBuilderRelationshipClassification<Address, Address, AddressXAddressQueryBuilder,
		AddressXAddress, UUID,AddressXAddressSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<AddressXAddress, Address> getPrimaryAttribute()
	{
		return AddressXAddress_.addressID;
	}
	
	@Override
	public SingularAttribute<AddressXAddress, Address> getSecondaryAttribute()
	{
		return AddressXAddress_.componentAddressID;
	}
}
