package com.guicedee.activitymaster.core.db.abstraction;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * @param <S>
 * @param <J>
 * @param <P>
 * @author Marc Magon
 * @since 08 Dec 2016
 */
@MappedSuperclass

public abstract class WarehouseClassificationRelationshipTypesTable<
		P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		J extends WarehouseClassificationRelationshipTypesTable<P, S, J, Q, T, I, ST, L, R>,
		Q extends QueryBuilderRelationshipClassificationTypes<P, S, Q, J, T, I, ST>,
		T extends ITypeValue<?>,
		I extends Serializable,
		ST extends WarehouseSecurityTable
		, L, R>
		extends WarehouseClassificationRelationshipTable<P, S, J, Q, I, ST, L, R>
{
	
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
	
	@Override
	@SuppressWarnings("unchecked")
	protected @NotNull Class<ST> findPersistentSecurityClass()
	{
		return (Class<ST>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[6];
	}
	
}
