package com.guicedee.activitymaster.fsdm.db.abstraction.assists;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseNameAndDescriptionTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.assists.QueryBuilderNameDescription;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.lang.reflect.ParameterizedType;

/**
 * @param <J>
 * @author Marc Magon
 * @version 1.0
 * @since 06 Dec 2016
 */
@MappedSuperclass()
public abstract class WarehouseNameDescriptionTable<
		J extends WarehouseNameDescriptionTable<J, Q, I,QS>,
		Q extends QueryBuilderNameDescription<Q, J, I>,
		I extends java.lang.String,
		QS extends IWarehouseSecurityTable<QS,?>>
		extends WarehouseCoreTable<J, Q, I,QS>
		implements IWarehouseNameAndDescriptionTable<J, Q, I>
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
