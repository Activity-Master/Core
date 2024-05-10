/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.resourceitem;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemTypeSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;


/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Resource", name = "ResourceItemTypeSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class ResourceItemTypeSecurityToken
		extends IWarehouseSecurityTable<ResourceItemTypeSecurityToken, ResourceItemTypeSecurityTokenQueryBuilder, String>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ResourceItemTypeSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ResourceItemTypeID",
	            referencedColumnName = "ResourceItemTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ResourceItemType base;
	
	public ResourceItemTypeSecurityToken()
	{
	
	}
	
	public ResourceItemTypeSecurityToken(java.lang.String resourceItemTypeSecurityTokenID)
	{
		this.id = resourceItemTypeSecurityTokenID;
	}
	
	public String toString()
	{
		return "ResourceItemTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public ResourceItemTypeSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ResourceItemType getBase()
	{
		return this.base;
	}
	
	public ResourceItemTypeSecurityToken setBase(ResourceItemType base)
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
		if (!(o instanceof ResourceItemTypeSecurityToken))
		{
			return false;
		}
		final ResourceItemTypeSecurityToken other = (ResourceItemTypeSecurityToken) o;
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
		return other instanceof ResourceItemTypeSecurityToken;
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
