package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.entityassist.BaseEntity;
import com.entityassist.querybuilder.QueryBuilder;

import java.io.Serializable;

public abstract class DefaultTimeQueryBuilder<J extends DefaultTimeQueryBuilder<J, E, I>, E extends BaseEntity<E, J, I>, I extends Serializable>
		extends QueryBuilder<J, E, I>
{
	public DefaultTimeQueryBuilder()
	{

	}
	
}
