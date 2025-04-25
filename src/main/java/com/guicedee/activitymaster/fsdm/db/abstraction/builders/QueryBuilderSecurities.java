/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSecurity;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import jakarta.persistence.metamodel.Attribute;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

/**
 * Default query builder for relationship tables
 *
 * @param <J> the type parameter
 * @param <E> the type parameter
 * @param <I> the type parameter
 * @author Marc Magon
 */
public abstract class QueryBuilderSecurities<J extends QueryBuilderSecurities<J, E, I>,
		E extends WarehouseSecurityTable<E, J, I>,
		I extends UUID>
		extends QueryBuilderSCD<J, E, I,J>
		implements IQueryBuilderSecurity<J,E,I>
{
	@SuppressWarnings("unchecked")
	@NotNull
	public J findLinkedSecurityToken(SecurityToken identityToken, I id)
	{
		where(getSecurityTokenAttribute(), Equals, identityToken);
		where(getMyAttribute(), Equals, id);
		return (J) this;
	}
	
	protected Attribute getSecurityTokenAttribute()
	{
		return getAttribute("securityTokenID");
	}
	
	protected Attribute getBaseEntityAttribute()
	{
		return getAttribute("base");
	}
	
	protected abstract Attribute getMyAttribute();
	
	public J findBySecurityToken(SecurityToken uuid)
	{
		where(getSecurityTokenAttribute(), Equals, uuid);
		return (J) this;
	}
	
	@SuppressWarnings("unchecked")
	@NotNull
	public J findLinkedSecurityToken(SecurityToken identityToken, WarehouseBaseTable id)
	{
		findLinkedSecurityTokens(id);
		Attribute securityAttribute = getSecurityTokenAttribute();
		where(securityAttribute, Equals, identityToken);
		return (J) this;
	}
	
	@SuppressWarnings("unchecked")
	@NotNull
	public J findLinkedSecurityTokens(WarehouseBaseTable id)
	{
		Attribute myAttribute = getMyAttribute();
		where(myAttribute, Equals, id);
		return (J) this;
	}
}
