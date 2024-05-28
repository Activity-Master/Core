package com.guicedee.activitymaster.fsdm.db.entities.resourceitem;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemDataSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Resource", name = "ResourceItemDataSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class ResourceItemDataSecurityToken
		extends WarehouseSecurityTable<ResourceItemDataSecurityToken, ResourceItemDataSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ResourceItemDataSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ResourceItemDataID",
	            referencedColumnName = "ResourceItemDataID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ResourceItemData base;
	
	public ResourceItemDataSecurityToken()
	{
	
	}
	
	public ResourceItemDataSecurityToken(java.lang.String resourceItemDataSecurityTokenID)
	{
		this.id = resourceItemDataSecurityTokenID;
	}
	
	public String toString()
	{
		return "ResourceItemDataSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public ResourceItemDataSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ResourceItemData getBase()
	{
		return this.base;
	}
	
	public ResourceItemDataSecurityToken setBase(ResourceItemData base)
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
		if (!(o instanceof ResourceItemDataSecurityToken))
		{
			return false;
		}
		final ResourceItemDataSecurityToken other = (ResourceItemDataSecurityToken) o;
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
		return other instanceof ResourceItemDataSecurityToken;
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
