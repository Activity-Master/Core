/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.rules;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.rules.builders.RulesTypeSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Rules",
       name = "RulesTypesSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class RulesTypeSecurityToken
		extends WarehouseSecurityTable<RulesTypeSecurityToken, RulesTypeSecurityTokenQueryBuilder, String>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesTypesSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "RulesTypesID",
	            referencedColumnName = "RulesTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private RulesType base;
	
	public RulesTypeSecurityToken()
	{
	
	}
	
	public RulesTypeSecurityToken(java.lang.String rulesTypesSecurityTokenID)
	{
		this.id = rulesTypesSecurityTokenID;
	}
	
	@Override
	public String toString()
	{
		return "RulesTypesSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	@Override
	public java.lang.String getId()
	{
		return this.id;
	}
	
	@Override
	public RulesTypeSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public RulesType getBase()
	{
		return this.base;
	}
	
	public RulesTypeSecurityToken setBase(RulesType base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof RulesTypeSecurityToken))
		{
			return false;
		}
		final RulesTypeSecurityToken other = (RulesTypeSecurityToken) o;
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
		return other instanceof RulesTypeSecurityToken;
	}
	
	@Override
	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}
