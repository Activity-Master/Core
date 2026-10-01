package com.guicedee.activitymaster.fsdm.db.entities.address.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddressTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.address.AddressType;

import java.util.UUID;

public class AddressTypeQueryBuilder
		extends QueryBuilderSCD<AddressTypeQueryBuilder, AddressType, UUID,
				AddressTypeSecurityTokenQueryBuilder>
		implements IAddressTypeQueryBuilder<AddressTypeQueryBuilder, AddressType>,
		           IQueryBuilderNamesAndDescriptions<AddressTypeQueryBuilder,AddressType,java.util.UUID>
{

}
