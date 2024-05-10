/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderClassifications;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderRelationships;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseCoreTable;



/**
 * @param <P>
 * @param <S>
 * @param <J>
 * @author Marc Magon
 * @since 01 May 2017
 */
public abstract class QueryBuilderRelationshipClassification<P extends WarehouseCoreTable<
		P, ?, java.lang.String,?>,
		S extends WarehouseCoreTable<S, ?, java.lang.String,?>,
		J extends QueryBuilderRelationshipClassification<P, S, J, E, I,QS>,
		E extends WarehouseClassificationRelationshipTable<P, S, E, J, I,?>,
		I extends java.lang.String,
		QS extends QueryBuilderSecurities<QS,?,?>>
		extends QueryBuilderRelationship<P, S, J, E, I,QS>
		implements IQueryBuilderRelationships<J, E, P, S, I>,
		           IQueryBuilderClassifications<J, E, I>
{
}
