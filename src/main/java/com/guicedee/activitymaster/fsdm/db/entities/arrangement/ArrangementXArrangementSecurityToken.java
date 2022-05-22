/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXArrangementSecurityTokenQueryBuilder;
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
@Table(schema = "Arrangement", name = "ArrangementXArrangementSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class ArrangementXArrangementSecurityToken
		extends WarehouseSecurityTable<ArrangementXArrangementSecurityToken, ArrangementXArrangementSecurityTokenQueryBuilder, UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementXArrangementSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
	@JoinColumn(name = "ArrangementXArrangementID",
	            referencedColumnName = "ArrangementXArrangementID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ArrangementXArrangement base;
	
	public ArrangementXArrangementSecurityToken()
	{
	
	}
	
	public ArrangementXArrangementSecurityToken(UUID arrangementXArrangementSecurityTokenID)
	{
		this.id = arrangementXArrangementSecurityTokenID;
	}
	
	public String toString()
	{
		return "ArrangementXArrangementSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public ArrangementXArrangementSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public ArrangementXArrangement getBase()
	{
		return this.base;
	}
	
	public ArrangementXArrangementSecurityToken setBase(ArrangementXArrangement base)
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
		if (!(o instanceof ArrangementXArrangementSecurityToken))
		{
			return false;
		}
		final ArrangementXArrangementSecurityToken other = (ArrangementXArrangementSecurityToken) o;
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
		return other instanceof ArrangementXArrangementSecurityToken;
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
