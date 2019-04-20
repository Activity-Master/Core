package com.armineasy.activitymaster.activitymaster.db.entities.address.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXGeography;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXGeographySecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXGeography_;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.Geography;

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
