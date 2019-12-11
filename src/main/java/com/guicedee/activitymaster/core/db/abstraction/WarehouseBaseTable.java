package com.guicedee.activitymaster.core.db.abstraction;

import com.entityassist.SCDEntity;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderDefault;

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

	private static final long serialVersionUID = 7208739285773494981L;

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
