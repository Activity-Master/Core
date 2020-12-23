/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyTypeSecurityTokenQueryBuilder;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Party",name = "InvolvedPartyTypeSecurityToken")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyTypeSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyTypeSecurityToken, InvolvedPartyTypeSecurityTokenQueryBuilder, java.util.UUID>
{

	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "InvolvedPartyTypeSecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@JoinColumn(name = "InvolvedPartyTypeID",
			referencedColumnName = "InvolvedPartyTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private InvolvedPartyType base;

	public InvolvedPartyTypeSecurityToken()
	{

	}

	public InvolvedPartyTypeSecurityToken(UUID involvedPartyTypeSecurityTokenID)
	{
		this.id = involvedPartyTypeSecurityTokenID;
	}

	public String toString()
	{
		return "InvolvedPartyTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public InvolvedPartyType getBase()
	{
		return this.base;
	}

	public InvolvedPartyTypeSecurityToken setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyTypeSecurityToken setBase(InvolvedPartyType base)
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
		if (!(o instanceof InvolvedPartyTypeSecurityToken))
		{
			return false;
		}
		final InvolvedPartyTypeSecurityToken other = (InvolvedPartyTypeSecurityToken) o;
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
		return other instanceof InvolvedPartyTypeSecurityToken;
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
