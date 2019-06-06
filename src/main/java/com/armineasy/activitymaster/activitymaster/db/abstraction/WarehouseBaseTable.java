package com.armineasy.activitymaster.activitymaster.db.abstraction;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderDefault;
import com.jwebmp.entityassist.SCDEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public abstract class WarehouseBaseTable<J extends WarehouseBaseTable<J, Q, I>,
		                                        Q extends QueryBuilderDefault<Q, J, I>, I extends Serializable>
		extends SCDEntity<J, Q, I>
		implements Serializable
{
	public @NotNull J updateNow()
	{
		return super.builder().updateNow((J) this);
	}
}
