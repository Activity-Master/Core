/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyXResourceItemSecurityTokenQueryBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Party",name = "InvolvedPartyXResourceItemSecurityToken")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyXResourceItemSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyXResourceItemSecurityToken, InvolvedPartyXResourceItemSecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyXResourceItemSecurityTokenID")
	private Long id;
	@JoinColumn(name = "InvolvedPartyXResourceItemID",
			referencedColumnName = "InvolvedPartyXResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private InvolvedPartyXResourceItem base;

	public InvolvedPartyXResourceItemSecurityToken()
	{

	}

	public InvolvedPartyXResourceItemSecurityToken(Long involvedPartyXResourceItemSecurityTokenID)
	{
		this.id = involvedPartyXResourceItemSecurityTokenID;
	}

	public String toString()
	{
		return "InvolvedPartyXResourceItemSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public InvolvedPartyXResourceItem getBase()
	{
		return this.base;
	}

	public InvolvedPartyXResourceItemSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyXResourceItemSecurityToken setBase(InvolvedPartyXResourceItem base)
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
		if (!(o instanceof InvolvedPartyXResourceItemSecurityToken))
		{
			return false;
		}
		final InvolvedPartyXResourceItemSecurityToken other = (InvolvedPartyXResourceItemSecurityToken) o;
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
		return other instanceof InvolvedPartyXResourceItemSecurityToken;
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
