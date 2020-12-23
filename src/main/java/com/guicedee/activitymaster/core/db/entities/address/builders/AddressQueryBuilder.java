package com.guicedee.activitymaster.core.db.entities.address.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IContainsClassificationsQueryBuilder;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IHasClassificationQueryBuilder;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IHasValueQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.address.AddressSecurityToken;
import com.guicedee.activitymaster.core.db.entities.address.AddressXClassification;

import java.util.UUID;

public class AddressQueryBuilder
		extends QueryBuilderTable<AddressQueryBuilder, Address, java.util.UUID, AddressSecurityToken>
		implements IContainsClassificationsQueryBuilder<AddressQueryBuilder, Address, java.util.UUID, AddressXClassification>,
		           IHasValueQueryBuilder<AddressQueryBuilder, Address, java.util.UUID>,
		           IHasClassificationQueryBuilder<AddressQueryBuilder,Address, UUID>
{

}
