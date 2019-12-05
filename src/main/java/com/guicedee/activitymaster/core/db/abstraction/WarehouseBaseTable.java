package com.guicedee.activitymaster.core.db.abstraction;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderDefault;
import com.entityassist.SCDEntity;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

@MappedSuperclass()
public abstract class WarehouseBaseTable<J extends WarehouseBaseTable<J, Q, I>,
		                                        Q extends QueryBuilderDefault<Q, J, I>, I extends Serializable>
		extends SCDEntity<J, Q, I>
		implements Serializable
{

	@SuppressWarnings("unchecked")
	public @NotNull J updateNow()
	{
		return super.builder()
		            .updateNow((J) this);
	}

	@SuppressWarnings("unchecked")
	public J expireIn(Duration duration)
	{
		setEffectiveToDate(LocalDateTime.now()
		                                .plus(duration));
		updateNow();
		return (J) this;
	}
}
