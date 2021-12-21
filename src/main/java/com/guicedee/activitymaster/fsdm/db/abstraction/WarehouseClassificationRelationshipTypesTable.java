package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTypeTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.lang.reflect.ParameterizedType;
import java.util.UUID;

/**
 * @param <S>
 * @param <J>
 * @param <P>
 * @author Marc Magon
 * @since 08 Dec 2016
 */
@MappedSuperclass

public abstract class WarehouseClassificationRelationshipTypesTable<
		P extends WarehouseCoreTable<P, ?, UUID>,
		S extends WarehouseCoreTable<S, ?, UUID>,
		J extends WarehouseClassificationRelationshipTypesTable<P, S, J, Q, I>,
		Q extends QueryBuilderRelationshipClassificationTypes<P, S, Q, J, I>,
		I extends UUID>
		extends WarehouseClassificationRelationshipTable<P, S, J, Q, I>
		implements IWarehouseRelationshipClassificationTypeTable<J, Q, P, S, I>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	public WarehouseClassificationRelationshipTypesTable()
	{
	
	}
	
	@Override
	@NotNull
	@SuppressWarnings("unchecked")
	public Class<I> getClassIDType()
	{
		return (Class<I>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[5];
	}
	
	@NotNull
	public Class<S> findSecurityClass()
	{
		return (Class<S>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[6];
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected @NotNull Class<Q> getClassQueryBuilderClass()
	{
		return (Class<Q>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
	}
	
}
