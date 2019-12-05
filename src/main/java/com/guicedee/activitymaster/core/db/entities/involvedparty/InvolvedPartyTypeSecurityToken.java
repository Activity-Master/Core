/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyTypeSecurityTokenQueryBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "InvolvedPartyTypeSecurityToken")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyTypeSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyTypeSecurityToken, InvolvedPartyTypeSecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyTypeSecurityTokenID")
	private Long id;

	@JoinColumn(name = "InvolvedPartyTypeID",
			referencedColumnName = "InvolvedPartyTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private InvolvedPartyType base;

	public InvolvedPartyTypeSecurityToken()
	{

	}

	public InvolvedPartyTypeSecurityToken(Long involvedPartyTypeSecurityTokenID)
	{
		this.id = involvedPartyTypeSecurityTokenID;
	}

	public String toString()
	{
		return "InvolvedPartyTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public InvolvedPartyType getBase()
	{
		return this.base;
	}

	public InvolvedPartyTypeSecurityToken setId(Long id)
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
