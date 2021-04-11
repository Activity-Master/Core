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

import java.util.UUID;

/**
 * @param <P>
 * @param <S>
 * @param <J>
 * @author Marc Magon
 * @since 01 May 2017
 */
public abstract class QueryBuilderRelationshipClassification<P extends WarehouseCoreTable<P,?, UUID>,
		S extends WarehouseCoreTable<S,?,UUID>,
		J extends QueryBuilderRelationshipClassification<P, S, J, E, I>,
		E extends WarehouseClassificationRelationshipTable<P, S, E, J, I>,
		I extends UUID>
		extends QueryBuilderRelationship<P, S, J, E, I>
		implements IQueryBuilderRelationships<J,E,P,S,I>,
		           IQueryBuilderClassifications<J,E,I>
{
}
