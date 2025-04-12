package com.guicedee.activitymaster.fsdm.db.entities.address.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.address.*;
import com.guicedee.activitymaster.fsdm.db.entities.geography.Geography;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class AddressXGeographyQueryBuilder
		extends QueryBuilderRelationshipClassification<Address, Geography, AddressXGeographyQueryBuilder,
		AddressXGeography, UUID,AddressXGeographySecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<AddressXGeography, Address> getPrimaryAttribute()
	{
		return AddressXGeography_.addressID;
	}
	
	@Override
	public SingularAttribute<AddressXGeography, Geography> getSecondaryAttribute()
	{
		return AddressXGeography_.geographyID;
	}
}
