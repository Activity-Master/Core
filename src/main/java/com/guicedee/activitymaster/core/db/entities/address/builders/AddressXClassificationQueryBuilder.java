package com.guicedee.activitymaster.core.db.entities.address.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.address.AddressXClassification;
import com.guicedee.activitymaster.core.db.entities.address.AddressXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.address.AddressXClassification_;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class AddressXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Address, Classification, AddressXClassificationQueryBuilder,
				                                              AddressXClassification, java.util.UUID, AddressXClassificationSecurityToken>
{
	@Override
	public SingularAttribute<AddressXClassification, Address> getPrimaryAttribute()
	{
		return AddressXClassification_.addressID;
	}

	@Override
	public  SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return AddressXClassification_.classificationID;
	}
}
