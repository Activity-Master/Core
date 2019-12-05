/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyOrganicTypeSecurityTokenQueryBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "InvolvedPartyOrganicTypeSecurityToken")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyOrganicTypeSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyOrganicTypeSecurityToken, InvolvedPartyOrganicTypeSecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyOrganicTypeSecurityTokenID")
	private Long id;

	@JoinColumn(name = "InvolvedPartyOrganicTypeID",
			referencedColumnName = "InvolvedPartyOrganicTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private InvolvedPartyOrganicType base;

	public InvolvedPartyOrganicTypeSecurityToken()
	{

	}

	public InvolvedPartyOrganicTypeSecurityToken(Long involvedPartyOrganicTypeSecurityTokenID)
	{
		this.id = involvedPartyOrganicTypeSecurityTokenID;
	}

	public String toString()
	{
		return "InvolvedPartyOrganicTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public InvolvedPartyOrganicType getBase()
	{
		return this.base;
	}

	public InvolvedPartyOrganicTypeSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyOrganicTypeSecurityToken setBase(InvolvedPartyOrganicType base)
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
		if (!(o instanceof InvolvedPartyOrganicTypeSecurityToken))
		{
			return false;
		}
		final InvolvedPartyOrganicTypeSecurityToken other = (InvolvedPartyOrganicTypeSecurityToken) o;
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
		return other instanceof InvolvedPartyOrganicTypeSecurityToken;
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
