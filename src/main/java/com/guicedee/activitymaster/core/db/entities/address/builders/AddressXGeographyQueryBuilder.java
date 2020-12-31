package com.guicedee.activitymaster.core.db.entities.address.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.address.AddressXGeography;
import com.guicedee.activitymaster.core.db.entities.address.AddressXGeographySecurityToken;
import com.guicedee.activitymaster.core.db.entities.address.AddressXGeography_;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class AddressXGeographyQueryBuilder
		extends QueryBuilderRelationshipClassification<Address, Geography, AddressXGeographyQueryBuilder,
				                                              AddressXGeography, java.util.UUID, AddressXGeographySecurityToken>
{
	@Override
	public SingularAttribute<AddressXGeography, Address> getPrimaryAttribute()
	{
		return AddressXGeography_.addressID;
	}

	@Override
	public  SingularAttribute<AddressXGeography, Geography> getSecondaryAttribute()
	{
		return AddressXGeography_.geographyID;
	}
}
