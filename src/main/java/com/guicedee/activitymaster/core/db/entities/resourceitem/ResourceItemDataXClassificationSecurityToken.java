package com.guicedee.activitymaster.core.db.entities.resourceitem;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemDataXClassificationSecurityTokenQueryBuilder;

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
@Table(schema="Resource",name = "ResourceItemDataXClassificationSecurityToken")
@XmlRootElement

@Access(FIELD)
public class ResourceItemDataXClassificationSecurityToken
		extends WarehouseSecurityTable<ResourceItemDataXClassificationSecurityToken, ResourceItemDataXClassificationSecurityTokenQueryBuilder, java.util.UUID>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "ResourceItemDataXClassificationSecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@JoinColumn(name = "ResourceItemDataXClassificationID",
			referencedColumnName = "ResourceItemDataXClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ResourceItemDataXClassification base;

	public ResourceItemDataXClassificationSecurityToken()
	{

	}

	public ResourceItemDataXClassificationSecurityToken(UUID resourceItemDataSecurityTokenID)
	{
		this.id = resourceItemDataSecurityTokenID;
	}

	public String toString()
	{
		return "ResourceItemDataXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public ResourceItemDataXClassification getBase()
	{
		return this.base;
	}

	public ResourceItemDataXClassificationSecurityToken setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public ResourceItemDataXClassificationSecurityToken setBase(ResourceItemDataXClassification base)
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
		if (!(o instanceof ResourceItemDataXClassificationSecurityToken))
		{
			return false;
		}
		final ResourceItemDataXClassificationSecurityToken other = (ResourceItemDataXClassificationSecurityToken) o;
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
		return other instanceof ResourceItemDataXClassificationSecurityToken;
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
