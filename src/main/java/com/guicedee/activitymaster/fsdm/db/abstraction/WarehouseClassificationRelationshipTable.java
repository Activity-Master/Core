package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
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

public abstract class WarehouseClassificationRelationshipTable<P extends WarehouseCoreTable<P, ?, UUID>,
		S extends WarehouseCoreTable<S, ?, UUID>,
		J extends WarehouseClassificationRelationshipTable<P, S, J, Q, I>,
		Q extends QueryBuilderRelationshipClassification<P, S, Q, J, I>,
		I extends UUID>
		extends WarehouseRelationshipTable<P, S, J, Q, I>
		implements IWarehouseRelationshipClassificationTable<J, Q, P, S, I>
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
