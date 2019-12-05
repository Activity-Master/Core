/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.resourceitem;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemTypeSecurityTokenQueryBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ResourceItemTypeSecurityToken")
@XmlRootElement

@Access(FIELD)
public class ResourceItemTypeSecurityToken
		extends WarehouseSecurityTable<ResourceItemTypeSecurityToken, ResourceItemTypeSecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ResourceItemTypeSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ResourceItemTypeID",
			referencedColumnName = "ResourceItemTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ResourceItemType base;

	public ResourceItemTypeSecurityToken()
	{

	}

	public ResourceItemTypeSecurityToken(Long resourceItemTypeSecurityTokenID)
	{
		this.id = resourceItemTypeSecurityTokenID;
	}

	public String toString()
	{
		return "ResourceItemTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public ResourceItemType getBase()
	{
		return this.base;
	}

	public ResourceItemTypeSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
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
