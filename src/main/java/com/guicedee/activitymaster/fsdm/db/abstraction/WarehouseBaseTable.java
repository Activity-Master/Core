package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.entityassist.SCDEntity;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseBaseTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderDefault;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.time.Duration;
import java.time.LocalDateTime;

@MappedSuperclass()
public abstract class WarehouseBaseTable<J extends WarehouseBaseTable<J, Q, I>,
		Q extends QueryBuilderDefault<Q, J, I>, I extends java.util.UUID>
		extends SCDEntity<J, Q, I>
		implements IWarehouseBaseTable<J, Q, I>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	public @NotNull J updateNow()
	{
		return super.builder()
		            .updateNow((J) this);
	}
	
	public J expireIn(Duration duration)
	{
		setEffectiveToDate(LocalDateTime.now()
		                                .plus(duration));
		updateNow();
		return (J) this;
	}
}
