package com.guicedee.activitymaster.core.db.abstraction.assists;

import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseNameAndDescriptionTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderNameDescription;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.lang.reflect.ParameterizedType;

/**
 * @param <J>
 *
 * @author Marc Magon
 * @version 1.0
 * @since 06 Dec 2016
 */
@MappedSuperclass()
public abstract class WarehouseNameDescriptionTable<J extends WarehouseNameDescriptionTable<J, Q, I>,
		                                                   Q extends QueryBuilderNameDescription<Q, J, I>,
		                                                   I extends java.util.UUID>
		extends WarehouseCoreTable<J, Q, I>
		implements IWarehouseNameAndDescriptionTable<J,Q,I>
{
	@Serial
	private static final long serialVersionUID = 1L;

	public WarehouseNameDescriptionTable()
	{
	}

	@Override
	@NotNull
	@SuppressWarnings("unchecked")
	protected Class<Q> getClassQueryBuilderClass()
	{
		return (Class<Q>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}

}
