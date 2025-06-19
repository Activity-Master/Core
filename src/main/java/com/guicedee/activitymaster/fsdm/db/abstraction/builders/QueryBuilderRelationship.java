/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderRelationships;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseRelationshipTable;

import java.io.Serializable;
import java.util.UUID;


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
public abstract class QueryBuilderRelationship<P extends WarehouseBaseTable<P, ?, ?>,
		S extends WarehouseBaseTable<S, ?, ?>,
		J extends QueryBuilderRelationship<P, S, J, E, I,QS>,
		E extends WarehouseRelationshipTable<P, S, E, J, I,?>,
		I extends java.util.UUID,
		QS extends QueryBuilderSecurities<QS,?,I>>
		extends QueryBuilderSCD<J, E, I,QS>
		implements IQueryBuilderRelationships<J, E, P, S, I>
{
	public QueryBuilderRelationship()
	{

	}
}
