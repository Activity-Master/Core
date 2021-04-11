/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.rules;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.rules.builders.RulesXProductSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.UUID;

import static jakarta.persistence.AccessType.FIELD;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Rules",name = "RulesXProductSecurityToken")
@XmlRootElement

@Access(FIELD)
public class RulesXProductSecurityToken
		extends WarehouseSecurityTable<RulesXProductSecurityToken, RulesXProductSecurityTokenQueryBuilder, UUID>
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "RulesXProductSecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;

	@JoinColumn(name = "RulesXProductID",
			referencedColumnName = "RulesXProductID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private RulesXProduct base;

	public RulesXProductSecurityToken()
	{

	}

	public RulesXProductSecurityToken(UUID RulesXProductSecurityTokenID)
	{
		this.id = RulesXProductSecurityTokenID;
	}

	public String toString()
	{
		return "RulesXProductSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public UUID getId()
	{
		return this.id;
	}

	public RulesXProduct getBase()
	{
		return this.base;
	}

	public RulesXProductSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}

	public RulesXProductSecurityToken setBase(RulesXProduct base)
	{
		this.base = base;
		return this;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof RulesXProductSecurityToken))
		{
			return false;
		}
		final RulesXProductSecurityToken other = (RulesXProductSecurityToken) o;
		if (!other.canEqual((Object) this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
		{
			return false;
		}
		return true;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof RulesXProductSecurityToken;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}
