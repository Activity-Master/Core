package com.guicedee.activitymaster.fsdm.db.entities.address.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddressQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.Address;
import com.guicedee.activitymaster.fsdm.db.entities.address.Address_;
import jakarta.validation.constraints.NotNull;

public class AddressQueryBuilder
		extends QueryBuilderTable<AddressQueryBuilder, Address, java.util.UUID>
		implements IAddressQueryBuilder<AddressQueryBuilder,Address>
{
	@Override
	public @NotNull AddressQueryBuilder withValue(Operand operand, String value)
	{
		where(Address_.value, operand, value);
		return this;
	}
}
