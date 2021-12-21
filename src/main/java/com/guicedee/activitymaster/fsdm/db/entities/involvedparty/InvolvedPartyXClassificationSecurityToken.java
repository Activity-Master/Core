/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXClassificationSecurityTokenQueryBuilder;
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
@Table(schema = "Party", name = "InvolvedPartyXClassificationSecurityToken")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyXClassificationSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyXClassificationSecurityToken, InvolvedPartyXClassificationSecurityTokenQueryBuilder, UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyXClassificationSecurityTokenID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	@JoinColumn(name = "InvolvedPartyXClassificationID",
	            referencedColumnName = "InvolvedPartyXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private InvolvedPartyXClassification base;
	
	public InvolvedPartyXClassificationSecurityToken()
	{
	
	}
	
	public InvolvedPartyXClassificationSecurityToken(UUID involvedPartyXClassificationSecurityTokenID)
	{
		this.id = involvedPartyXClassificationSecurityTokenID;
	}
	
	public String toString()
	{
		return "InvolvedPartyXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public InvolvedPartyXClassificationSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedPartyXClassification getBase()
	{
		return this.base;
	}
	
	public InvolvedPartyXClassificationSecurityToken setBase(InvolvedPartyXClassification base)
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
		if (!(o instanceof InvolvedPartyXClassificationSecurityToken))
		{
			return false;
		}
		final InvolvedPartyXClassificationSecurityToken other = (InvolvedPartyXClassificationSecurityToken) o;
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
		return other instanceof InvolvedPartyXClassificationSecurityToken;
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
