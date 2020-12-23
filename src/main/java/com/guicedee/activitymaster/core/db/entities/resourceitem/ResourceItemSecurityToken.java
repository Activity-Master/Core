package com.guicedee.activitymaster.core.db.entities.resourceitem;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemSecurityTokenQueryBuilder;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Resource",name = "ResourceItemSecurityToken")
@XmlRootElement

@Access(FIELD)
public class ResourceItemSecurityToken
		extends WarehouseSecurityTable<ResourceItemSecurityToken, ResourceItemSecurityTokenQueryBuilder, java.util.UUID>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
			name = "ResourceItemSecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@JoinColumn(name = "ResourceItemID",
			referencedColumnName = "ResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ResourceItem base;

	public ResourceItemSecurityToken()
	{

	}

	public ResourceItemSecurityToken(UUID resourceItemSecurityTokenID)
	{
		this.id = resourceItemSecurityTokenID;
	}

	public String toString()
	{
		return "ResourceItemSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public ResourceItem getBase()
	{
		return this.base;
	}

	public ResourceItemSecurityToken setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public ResourceItemSecurityToken setBase(ResourceItem base)
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
		if (!(o instanceof ResourceItemSecurityToken))
		{
			return false;
		}
		final ResourceItemSecurityToken other = (ResourceItemSecurityToken) o;
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
		return other instanceof ResourceItemSecurityToken;
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
