/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartySecurityTokenQueryBuilder;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "InvolvedPartyXInvolvedPartySecurityToken")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyXInvolvedPartySecurityToken
		extends WarehouseSecurityTable<InvolvedPartyXInvolvedPartySecurityToken, InvolvedPartyXInvolvedPartySecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyXInvolvedPartySecurityTokenID")
	private Long id;
	@JoinColumn(name = "InvolvedPartyXInvolvedPartyID",
			referencedColumnName = "InvolvedPartyXInvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private InvolvedPartyXInvolvedParty base;

	public InvolvedPartyXInvolvedPartySecurityToken()
	{

	}

	public InvolvedPartyXInvolvedPartySecurityToken(Long involvedPartyXInvolvedPartySecurityTokenID)
	{
		this.id = involvedPartyXInvolvedPartySecurityTokenID;
	}

	public String toString()
	{
		return "InvolvedPartyXInvolvedPartySecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public InvolvedPartyXInvolvedParty getBase()
	{
		return this.base;
	}

	public InvolvedPartyXInvolvedPartySecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyXInvolvedPartySecurityToken setBase(InvolvedPartyXInvolvedParty base)
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
		if (!(o instanceof InvolvedPartyXInvolvedPartySecurityToken))
		{
			return false;
		}
		final InvolvedPartyXInvolvedPartySecurityToken other = (InvolvedPartyXInvolvedPartySecurityToken) o;
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
		return other instanceof InvolvedPartyXInvolvedPartySecurityToken;
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
