package com.guicedee.activitymaster.core.db.entities.address.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilder;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.address.AddressSecurityToken;
import com.guicedee.activitymaster.core.db.entities.address.Address_;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;

import static com.entityassist.enumerations.Operand.*;

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
