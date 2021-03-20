package com.guicedee.activitymaster.core.db.entities.address.builders;

import com.guicedee.activitymaster.client.services.builders.warehouse.address.IAddressQueryBuilder;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.entities.address.Address;

public class AddressQueryBuilder
		extends QueryBuilderTable<AddressQueryBuilder, Address, java.util.UUID>
		implements IAddressQueryBuilder<AddressQueryBuilder,Address>
{

}
