/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.abstraction.builders;

import com.guicedee.activitymaster.client.services.builders.IQueryBuilderClassifications;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;

import java.util.UUID;

/**
 * @param <P>
 * @param <S>
 * @param <J>
 * @author Marc Magon
 * @since 01 May 2017
 */
public abstract class QueryBuilderRelationshipClassificationTypes<
		P extends WarehouseCoreTable<P,?,UUID>,
		S extends WarehouseCoreTable<S,?,UUID>,
		J extends QueryBuilderRelationshipClassificationTypes<P, S, J, E,  I>,
		E extends WarehouseClassificationRelationshipTypesTable<P, S, E, J, I>,
		I extends java.util.UUID>
		extends QueryBuilderRelationshipClassification<P, S, J, E, I>
		implements IQueryBuilderClassifications<J,E,I>
{
	
	public abstract J withType(String typeValue, ISystems<?,?> system, UUID... identityToken);
	
}
