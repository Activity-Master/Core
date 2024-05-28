package com.guicedee.activitymaster.fsdm.db.entities.resourceitem;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemXResourceItemSecurityTokenQueryBuilder;
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
@Table(schema = "Resource",
       name = "ResourceItemXResourceItemSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class ResourceItemXResourceItemSecurityToken
		extends WarehouseSecurityTable<ResourceItemXResourceItemSecurityToken, ResourceItemXResourceItemSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ResourceItemXResourceItemSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ResourceItemXResourceItemID",
	            referencedColumnName = "ResourceItemXResourceItemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ResourceItemXResourceItem base;
	
	public ResourceItemXResourceItemSecurityToken()
	{
	
	}
	
	public ResourceItemXResourceItemSecurityToken(java.lang.String productXResourceItemSecurityTokenID)
	{
		this.id = productXResourceItemSecurityTokenID;
	}
	
	public String toString()
	{
		return "ResourceItemXResourceItemSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public ResourceItemXResourceItemSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ResourceItemXResourceItem getBase()
	{
		return this.base;
	}
	
	public ResourceItemXResourceItemSecurityToken setBase(ResourceItemXResourceItem base)
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
		if (!(o instanceof ResourceItemXResourceItemSecurityToken))
		{
			return false;
		}
		final ResourceItemXResourceItemSecurityToken other = (ResourceItemXResourceItemSecurityToken) o;
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
		return other instanceof ResourceItemXResourceItemSecurityToken;
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
