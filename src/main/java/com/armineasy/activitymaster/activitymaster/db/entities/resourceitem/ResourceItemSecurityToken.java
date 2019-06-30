package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemSecurityTokenQueryBuilder;
import lombok.experimental.Accessors;

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
@Table(name = "ResourceItemSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ResourceItemSecurityToken
		extends WarehouseSecurityTable<ResourceItemSecurityToken, ResourceItemSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ResourceItemSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ResourceItemID",
			referencedColumnName = "ResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ResourceItem base;

	public ResourceItemSecurityToken()
	{

	}

	public ResourceItemSecurityToken(Long resourceItemSecurityTokenID)
	{
		this.id = resourceItemSecurityTokenID;
	}

	public String toString()
	{
		return "ResourceItemSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public ResourceItem getBase()
	{
		return this.base;
	}

	public ResourceItemSecurityToken setId(Long id)
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
