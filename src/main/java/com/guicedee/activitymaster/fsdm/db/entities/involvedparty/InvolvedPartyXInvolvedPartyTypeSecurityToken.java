/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyTypeSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Party", name = "InvolvedPartyXInvolvedPartyTypeSecurityToken")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyXInvolvedPartyTypeSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyXInvolvedPartyTypeSecurityToken, InvolvedPartyXInvolvedPartyTypeSecurityTokenQueryBuilder, UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyXInvolvedPartyTypeSecurityTokenID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	@JoinColumn(name = "InvolvedPartyXInvolvedPartyTypeID",
	            referencedColumnName = "InvolvedPartyXInvolvedPartyTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private InvolvedPartyXInvolvedPartyType base;
	
	public InvolvedPartyXInvolvedPartyTypeSecurityToken()
	{
	
	}
	
	public InvolvedPartyXInvolvedPartyTypeSecurityToken(UUID involvedPartyXInvolvedPartyTypeSecurityTokenID)
	{
		this.id = involvedPartyXInvolvedPartyTypeSecurityTokenID;
	}
	
	public String toString()
	{
		return "InvolvedPartyXInvolvedPartyTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public InvolvedPartyXInvolvedPartyTypeSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedPartyXInvolvedPartyType getBase()
	{
		return this.base;
	}
	
	public InvolvedPartyXInvolvedPartyTypeSecurityToken setBase(InvolvedPartyXInvolvedPartyType base)
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
		if (!(o instanceof InvolvedPartyXInvolvedPartyTypeSecurityToken))
		{
			return false;
		}
		final InvolvedPartyXInvolvedPartyTypeSecurityToken other = (InvolvedPartyXInvolvedPartyTypeSecurityToken) o;
		if (!other.canEqual(this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		return this$id == null ? other$id == null : this$id.equals(other$id);
	}
	
	protected boolean canEqual(final Object other)
	{
		return other instanceof InvolvedPartyXInvolvedPartyTypeSecurityToken;
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
