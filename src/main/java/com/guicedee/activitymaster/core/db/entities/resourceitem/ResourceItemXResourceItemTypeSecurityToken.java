/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.resourceitem;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemXResourceItemTypeSecurityTokenQueryBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Resource",name = "ResourceItemXResourceItemTypeSecurityToken")
@XmlRootElement

@Access(FIELD)
public class ResourceItemXResourceItemTypeSecurityToken
		extends WarehouseSecurityTable<ResourceItemXResourceItemTypeSecurityToken, ResourceItemXResourceItemTypeSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ResourceItemXResourceItemTypeSecurityTokenID")
	private Long id;
	@JoinColumn(name = "ResourceItemXResourceItemTypeID",
			referencedColumnName = "ResourceItemXResourceItemTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ResourceItemXResourceItemType base;

	public ResourceItemXResourceItemTypeSecurityToken()
	{

	}

	public ResourceItemXResourceItemTypeSecurityToken(Long resourceItemXResourceItemTypeSecurityTokenID)
	{
		this.id = resourceItemXResourceItemTypeSecurityTokenID;
	}

	public String toString()
	{
		return "ResourceItemXResourceItemTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public ResourceItemXResourceItemType getBase()
	{
		return this.base;
	}

	public ResourceItemXResourceItemTypeSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ResourceItemXResourceItemTypeSecurityToken setBase(ResourceItemXResourceItemType base)
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
		if (!(o instanceof ResourceItemXResourceItemTypeSecurityToken))
		{
			return false;
		}
		final ResourceItemXResourceItemTypeSecurityToken other = (ResourceItemXResourceItemTypeSecurityToken) o;
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
		return other instanceof ResourceItemXResourceItemTypeSecurityToken;
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
