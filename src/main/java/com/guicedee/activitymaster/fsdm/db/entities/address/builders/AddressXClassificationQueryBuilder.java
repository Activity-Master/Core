package com.guicedee.activitymaster.fsdm.db.entities.address.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.address.*;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import jakarta.persistence.metamodel.SingularAttribute;

public class AddressXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Address, Classification, AddressXClassificationQueryBuilder,
		AddressXClassification, java.util.UUID>
{
	@Override
	public SingularAttribute<AddressXClassification, Address> getPrimaryAttribute()
	{
		return AddressXClassification_.addressID;
	}
	
	@Override
	public SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return AddressXClassification_.classificationID;
	}
}
