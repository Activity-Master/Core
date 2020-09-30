/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.abstraction.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IHasValueQueryBuilder;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IHasClassificationQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.guicedinjection.GuiceContext;

import java.io.Serializable;

/**
 * @param <P>
 * @param <S>
 * @param <J>
 * @author Marc Magon
 * @since 01 May 2017
 */
public abstract class QueryBuilderRelationshipClassification<P extends WarehouseCoreTable, S extends WarehouseCoreTable,
		J extends QueryBuilderRelationshipClassification<P, S, J, E, I, ST>,
		E extends WarehouseClassificationRelationshipTable<P, S, E, J, I, ST, ?, ?>,
		I extends Serializable, ST extends WarehouseSecurityTable>
		extends QueryBuilderRelationship<P, S, J, E, I, ST>
		implements IHasClassificationQueryBuilder<J, E, I>,
		           IHasValueQueryBuilder<J,E,I>
{
	@Override
	public boolean onCreate(E entity)
	{
		if (entity.getClassificationID() == null)
		{
			IClassificationService<?> classificationService = GuiceContext.get(IClassificationService.class);
			entity.setClassificationID((Classification) classificationService.find(Classifications.NoClassification, entity.getEnterpriseID()));
		}
		return super.onCreate(entity);
	}
}
