package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemDataXClassificationSecurityTokenQueryBuilder;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ResourceItemDataXClassificationSecurityToken")
@XmlRootElement

@Access(FIELD)
public class ResourceItemDataXClassificationSecurityToken
		extends WarehouseSecurityTable<ResourceItemDataXClassificationSecurityToken, ResourceItemDataXClassificationSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ResourceItemDataXClassificationSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ResourceItemDataXClassificationID",
			referencedColumnName = "ResourceItemDataXClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ResourceItemDataXClassification base;

	public ResourceItemDataXClassificationSecurityToken()
	{

	}

	public ResourceItemDataXClassificationSecurityToken(Long resourceItemDataSecurityTokenID)
	{
		this.id = resourceItemDataSecurityTokenID;
	}

	public String toString()
	{
		return "ResourceItemDataXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public ResourceItemDataXClassification getBase()
	{
		return this.base;
	}

	public ResourceItemDataXClassificationSecurityToken setId(Long id)
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
