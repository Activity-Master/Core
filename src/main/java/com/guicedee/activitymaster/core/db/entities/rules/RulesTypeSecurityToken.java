/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.rules;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesTypeSecurityTokenQueryBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Rules",
       name = "RulesTypesSecurityToken")
@XmlRootElement

@Access(FIELD)
public class RulesTypeSecurityToken
		extends WarehouseSecurityTable<RulesTypeSecurityToken, RulesTypeSecurityTokenQueryBuilder, Long>
{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
	        name = "RulesTypesSecurityTokenID")
	private Long id;
	
	@JoinColumn(name = "RulesTypesID",
	            referencedColumnName = "RulesTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private RulesType base;
	
	public RulesTypeSecurityToken()
	{
	
	}
	
	public RulesTypeSecurityToken(Long rulesTypesSecurityTokenID)
	{
		this.id = rulesTypesSecurityTokenID;
	}
	
	@Override
	public String toString()
	{
		return "RulesTypesSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	@Override
	public Long getId()
	{
		return this.id;
	}
	
	public RulesType getBase()
	{
		return this.base;
	}
	
	@Override
	public RulesTypeSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
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
