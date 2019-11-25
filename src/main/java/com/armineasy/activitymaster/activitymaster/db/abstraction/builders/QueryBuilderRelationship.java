/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.abstraction.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;

import javax.persistence.metamodel.Attribute;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import static com.entityassist.enumerations.Operand.*;

/**
 * Default query builder for relationship tables
 *
 * @param <P>
 * 		the type parameter
 * @param <S>
 * 		the type parameter
 * @param <J>
 * 		the type parameter
 * @param <E>
 * 		the type parameter
 * @param <I>
 * 		the type parameter
 *
 * @author Marc Magon
 */
public abstract class QueryBuilderRelationship<P extends WarehouseCoreTable,
		                                              S extends WarehouseCoreTable,
		                                              J extends QueryBuilderRelationship<P, S, J, E, I, ST>,
		                                              E extends WarehouseRelationshipTable<P, S, E, J, I, ST,?,?>,
		                                              I extends Serializable,
		                                              ST extends WarehouseSecurityTable>
		extends QueryBuilder<J, E, I, ST>
{
	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J findLink(P parent, S child)
	{
		where(getPrimaryAttribute(), Equals, parent);
		where(getSecondaryAttribute(), Equals, child);
		return (J) this;
	}

	public abstract Attribute getPrimaryAttribute();

	public abstract Attribute getSecondaryAttribute();

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J findLink(P parent, S child, String value)
	{
		where(getPrimaryAttribute(), Equals, parent);
		where(getSecondaryAttribute(), Equals, child);
		withValue(value);
		return (J) this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J withValue(String value)
	{
		if (value != null)
		{
			where(getAttribute("value"), Equals, value);
		}
		return (J) this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J findChildLink(S child)
	{
		return findChildLink(child, null);
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J findChildLink(S child, String value)
	{
		where(getSecondaryAttribute(), Equals, child);
		if (value != null)
		{
			withValue(value);
		}
		return (J) this;
	}


	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J findParentLink(P child)
	{
		return findParentLink(child, null);
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J findParentLink(P parent, String value)
	{
		where(getPrimaryAttribute(), Equals, parent);
		if (value != null)
		{
			withValue(value);
		}
		return (J) this;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Class<ST> findSecurityClass()
	{
		return (Class<ST>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[5];
	}

}
