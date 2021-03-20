package com.guicedee.activitymaster.core.db.abstraction;

import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import jakarta.persistence.*;

import java.io.Serial;
import java.util.UUID;

/**
 * @param <S>
 * @param <J>
 * @param <P>
 * @author Marc Magon
 * @since 08 Dec 2016
 */
@MappedSuperclass

public abstract class WarehouseClassificationRelationshipTable <P extends WarehouseCoreTable<P,?, UUID>,
		S extends WarehouseCoreTable<S,?,UUID>,
		J extends WarehouseClassificationRelationshipTable<P, S, J, Q, I>,
		Q extends QueryBuilderRelationshipClassification<P, S, Q, J, I>,
		I extends java.util.UUID>
		extends WarehouseRelationshipTable<P, S, J, Q, I>
		implements IWarehouseRelationshipClassificationTable<J,Q,P,S,I>
{
	
	@Serial
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
	public J setClassificationID(IClassification classificationID)
	{
		this.classificationID = (Classification) classificationID;
		return (J)this;
	}
	
	@Override
	public Classification getClassificationID()
	{
		return classificationID;
	}
	
	/*	@Override
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
	*/
/*	public IClassification<?,?> getClassificationID()
	{
		return this.classificationID;
	}
	
	public J setClassificationID(IClassification<?,?> classificationID)
	{
		this.classificationID = (Classification) classificationID;
		return (J) this;
	}*/
}
