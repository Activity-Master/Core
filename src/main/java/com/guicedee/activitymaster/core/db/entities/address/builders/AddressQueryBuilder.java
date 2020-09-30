package com.guicedee.activitymaster.core.db.entities.address.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IContainsClassificationsQueryBuilder;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IHasClassificationQueryBuilder;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IHasValueQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.address.AddressSecurityToken;
import com.guicedee.activitymaster.core.db.entities.address.AddressXClassification;

public class AddressQueryBuilder
		extends QueryBuilderTable<AddressQueryBuilder, Address, Long, AddressSecurityToken>
		implements IContainsClassificationsQueryBuilder<AddressQueryBuilder, Address, Long, AddressXClassification>,
		           IHasValueQueryBuilder<AddressQueryBuilder, Address, Long>,
		           IHasClassificationQueryBuilder<AddressQueryBuilder,Address,Long>
{

}
