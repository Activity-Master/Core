/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderRelationships;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseRelationshipTable;



/**
 * Default query builder for relationship tables
 *
 * @param <P> the type parameter
 * @param <S> the type parameter
 * @param <J> the type parameter
 * @param <E> the type parameter
 * @param <I> the type parameter
 * @author Marc Magon
 */
public abstract class QueryBuilderRelationship<P extends WarehouseBaseTable<P, ?, java.lang.String>,
		S extends WarehouseBaseTable<S, ?, java.lang.String>,
		J extends QueryBuilderRelationship<P, S, J, E, I,QS>,
		E extends WarehouseRelationshipTable<P, S, E, J, I,?>,
		I extends java.lang.String,
		QS extends QueryBuilderSecurities<QS,?,I>>
		extends QueryBuilderSCD<J, E, I,QS>
		implements IQueryBuilderRelationships<J, E, P, S, I>
{
	public QueryBuilderRelationship()
	{

	}
}
