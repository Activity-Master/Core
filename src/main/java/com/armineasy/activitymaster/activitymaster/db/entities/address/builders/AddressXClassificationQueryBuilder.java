package com.armineasy.activitymaster.activitymaster.db.entities.address.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXClassification_;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;

import javax.persistence.metamodel.Attribute;

public class AddressXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Address, Classification, AddressXClassificationQueryBuilder,
				                                              AddressXClassification, Long, AddressXClassificationSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return AddressXClassification_.addressID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return AddressXClassification_.classificationID;
	}
}
