package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTypeTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
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
		P extends WarehouseCoreTable<P, ?, java.lang.String,?>,
		S extends WarehouseCoreTable<S, ?, java.lang.String,?>,
		J extends WarehouseClassificationRelationshipTypesTable<P, S, J, Q, I,QS>,
		Q extends QueryBuilderRelationshipClassificationTypes<P, S, Q, J, I,?>,
		I extends java.lang.String,
		QS extends WarehouseSecurityTable<QS,?,I>>
		extends WarehouseClassificationRelationshipTable<P, S, J, Q, I,QS>
		implements IWarehouseRelationshipClassificationTypeTable<J, Q, P, S, I,QS>
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
	public Class<QS> findSecurityClass()
	{
		return (Class<QS>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[6];
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected @NotNull Class<Q> getClassQueryBuilderClass()
	{
		return (Class<Q>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
	}
	
}
