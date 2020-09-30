package com.guicedee.activitymaster.core.db.abstraction;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

public abstract class WarehouseClassificationRelationshipTable<P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		J extends WarehouseClassificationRelationshipTable<P, S, J, Q, I, ST, L, R>,
		Q extends QueryBuilderRelationshipClassification<P, S, Q, J, I, ST>,
		I extends Serializable,
		ST extends WarehouseSecurityTable
		, L, R>
		extends WarehouseRelationshipTable<P, S, J, Q, I, ST, L, R>
{
	
	private static final long serialVersionUID = 1L;
	
	@JoinColumn(name = "ClassificationID",
	            referencedColumnName = "ClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Classification classificationID;
	
	public WarehouseClassificationRelationshipTable()
	{
	
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
		return (Class<ST>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[5];
	}
	
	public Classification getClassificationID()
	{
		return this.classificationID;
	}
	
	public J setClassificationID(Classification classificationID)
	{
		this.classificationID = classificationID;
		return (J) this;
	}
}
