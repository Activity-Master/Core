package com.armineasy.activitymaster.activitymaster.db.entities.address.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.address.Address_;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;

import static com.jwebmp.entityassist.enumerations.Operand.*;

public class AddressQueryBuilder
		extends QueryBuilder<AddressQueryBuilder, Address, Long, AddressSecurityToken>
{


	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public AddressQueryBuilder withValue(String value)
	{
		//where(Address_.value Equals, value);
		return this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public AddressQueryBuilder withClassification(Classification classification)
	{
		where(Address_.classification, Equals, classification);
		return this;
	}
}
