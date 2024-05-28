package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import jakarta.persistence.*;
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

public abstract class WarehouseClassificationRelationshipTable<
		P extends WarehouseCoreTable<P, ?, java.lang.String,?>,
		S extends WarehouseCoreTable<S, ?, java.lang.String,?>,
		J extends WarehouseClassificationRelationshipTable<P, S, J, Q, I,QS>,
		Q extends QueryBuilderRelationshipClassification<P, S, Q, J, I,?>,
		I extends java.lang.String,
		QS extends WarehouseSecurityTable<QS,?,I>>
		extends WarehouseRelationshipTable<P, S, J, Q, I,QS>
		implements IWarehouseRelationshipClassificationTable<J, Q, P, S, I,QS>
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
	
	@NotNull
	public Class<QS> findSecurityClass()
	{
		return (Class<QS>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[5];
	}
	
	
	@Override
	public Classification getClassificationID()
	{
		return classificationID;
	}
	
	@Override
	public J setClassificationID(IClassification classificationID)
	{
		this.classificationID = (Classification) classificationID;
		return (J) this;
	}
}
