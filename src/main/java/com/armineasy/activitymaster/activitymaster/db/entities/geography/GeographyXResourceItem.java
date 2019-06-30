/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.geography;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.builders.GeographyXResourceItemQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "GeographyXResourceItem")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class GeographyXResourceItem
		extends WarehouseClassificationRelationshipTable<Geography, ResourceItem, GeographyXResourceItem, GeographyXResourceItemQueryBuilder, Long, GeographyXResourceItemSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "GeographyXResourceItemID")
	private Long id;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<GeographyXResourceItemSecurityToken> securities;

	@JoinColumn(name = "GeographyID",
			referencedColumnName = "GeographyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Geography geographyID;
	@JoinColumn(name = "ResourceItemID",
			referencedColumnName = "ResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ResourceItem resourceItemID;

	public GeographyXResourceItem()
	{

	}

	public GeographyXResourceItem(Long geographyXResourceItemID)
	{
		this.id = geographyXResourceItemID;
	}

	@Override
	protected GeographyXResourceItemSecurityToken configureDefaultsForNewToken(GeographyXResourceItemSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "GeographyXResourceItem(id=" + this.getId() + ", securities=" + this.getSecurities() + ", geographyID=" + this.getGeographyID() + ", resourceItemID=" +
		       this.getResourceItemID() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public List<GeographyXResourceItemSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public Geography getGeographyID()
	{
		return this.geographyID;
	}

	public ResourceItem getResourceItemID()
	{
		return this.resourceItemID;
	}

	public GeographyXResourceItem setId(Long id)
	{
		this.id = id;
		return this;
	}

	public GeographyXResourceItem setSecurities(List<GeographyXResourceItemSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public GeographyXResourceItem setGeographyID(Geography geographyID)
	{
		this.geographyID = geographyID;
		return this;
	}

	public GeographyXResourceItem setResourceItemID(ResourceItem resourceItemID)
	{
		this.resourceItemID = resourceItemID;
		return this;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof GeographyXResourceItem))
		{
			return false;
		}
		final GeographyXResourceItem other = (GeographyXResourceItem) o;
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
		return other instanceof GeographyXResourceItem;
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
