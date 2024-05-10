/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderClassifications;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseCoreTable;



/**
 * @param <P>
 * @param <S>
 * @param <J>
 * @author Marc Magon
 * @since 01 May 2017
 */
public abstract class QueryBuilderRelationshipClassificationTypes<
		P extends WarehouseCoreTable<P, ?, java.lang.String,?>,
		S extends WarehouseCoreTable<S, ?, java.lang.String,?>,
		J extends QueryBuilderRelationshipClassificationTypes<P, S, J, E, I,QS>,
		E extends WarehouseClassificationRelationshipTypesTable<P, S, E, J, I,?>,
		I extends java.lang.String,
		QS extends QueryBuilderSecurities<QS,?,?>
		>
		extends QueryBuilderRelationshipClassification<P, S, J, E, I,QS>
		implements IQueryBuilderClassifications<J, E, I>
{
	
	public abstract J withType(String typeValue, ISystems<?, ?> system, java.util.UUID... identityToken);
	
}
