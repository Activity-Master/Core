package com.guicedee.activitymaster.core.db.entities.address.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.address.AddressXGeography;
import com.guicedee.activitymaster.core.db.entities.address.AddressXGeographySecurityToken;
import com.guicedee.activitymaster.core.db.entities.address.AddressXGeography_;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;

import javax.persistence.metamodel.Attribute;

public class AddressXGeographyQueryBuilder
		extends QueryBuilderRelationshipClassification<Address, Geography, AddressXGeographyQueryBuilder,
				                                              AddressXGeography, Long, AddressXGeographySecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return AddressXGeography_.addressID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return AddressXGeography_.geographyID;
	}
}
