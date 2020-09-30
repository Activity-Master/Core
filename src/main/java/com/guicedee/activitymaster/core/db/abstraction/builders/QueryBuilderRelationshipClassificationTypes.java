/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.abstraction.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IHasClassificationQueryBuilder;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.UUID;

/**
 * @param <P>
 * @param <S>
 * @param <J>
 * @author Marc Magon
 * @since 01 May 2017
 */
public abstract class QueryBuilderRelationshipClassificationTypes<
		P extends WarehouseCoreTable,
		S extends WarehouseCoreTable,
		J extends QueryBuilderRelationshipClassificationTypes<P, S, J, E, T, I, ST>,
		E extends WarehouseClassificationRelationshipTypesTable<P, S, E, J, T, I, ST, ?, ?>,
		T extends ITypeValue<?>,
		I extends Serializable,
		ST extends WarehouseSecurityTable>
		extends QueryBuilderRelationshipClassification<P, S, J, E, I, ST>
		implements IHasClassificationQueryBuilder<J, E, I>
{
	public J withType(T typeValue, IEnterpriseName<?> enterprise, UUID... identityToken)
	{
		return withType(typeValue, enterprise.getEnterprise(), identityToken);
	}
	
	public J withType(T typeValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		return withType(typeValue.toString(), enterprise, identityToken);
	}
	
	public abstract J withType(String typeValue, IEnterprise<?> enterprise, UUID... identityToken);
	
	@Override
	public Class<ST> findSecurityClass()
	{
		return (Class<ST>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[6];
	}
}
