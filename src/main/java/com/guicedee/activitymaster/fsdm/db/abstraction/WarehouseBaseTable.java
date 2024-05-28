package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.entityassist.SCDEntity;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseBaseTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderDefault;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.time.Duration;

@MappedSuperclass()
public abstract class WarehouseBaseTable<J extends WarehouseBaseTable<J, Q, I>,
		Q extends QueryBuilderDefault<Q, J, I>, I extends java.lang.String>
		extends SCDEntity<J, Q, I>
		implements IWarehouseBaseTable<J, Q, I>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	public J expireIn(Duration duration)
	{
		setEffectiveToDate(QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow())
		                                                              .plus(duration));
		update();
		return (J) this;
	}
	
	@Override
	public @NotNull boolean isFake()
	{
		return getId() == null;
	}
	
	@Override
	public @NotNull J persist()
	{
		return super.persist();
	}
}
