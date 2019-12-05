package com.guicedee.activitymaster.core.db.abstraction.assists;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderNameDescription;
import com.guicedee.activitymaster.core.services.capabilities.INameAndDescription;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @param <S>
 * @param <J>
 *
 * @author GedMarc
 * @version 1.0
 * @since 06 Dec 2016
 */
@MappedSuperclass()
public abstract class WarehouseNameDescriptionTable<J extends WarehouseNameDescriptionTable<J, Q, I, S>,
		                                                   Q extends QueryBuilderNameDescription<Q, J, I, S>,
		                                                   I extends Serializable,
		                                                   S extends WarehouseSecurityTable>
		extends WarehouseCoreTable<J, Q, I, S>
		implements INameAndDescription<J>
{
	private static final long serialVersionUID = 1L;

	public WarehouseNameDescriptionTable()
	{
	}

	@Override
	@NotNull
	@SuppressWarnings("unchecked")
	protected Class<Q> getClassQueryBuilderClass()
	{
		Type type = getClass().getGenericSuperclass();
		return (Class<Q>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}


}
