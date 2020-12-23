/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.geography;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.geography.builders.GeographyXResourceItemQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IGeography;
import com.guicedee.activitymaster.core.services.dto.IResourceItem;
import com.guicedee.activitymaster.core.services.dto.ISystems;


import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Geography",name = "GeographyXResourceItem")
@XmlRootElement

@Access(FIELD)
public class GeographyXResourceItem
		extends WarehouseClassificationRelationshipTable<Geography,
				                                                ResourceItem,
				                                                GeographyXResourceItem,
				                                                GeographyXResourceItemQueryBuilder,
				                                                java.util.UUID,
				                                                GeographyXResourceItemSecurityToken,
				                                                IGeography<?>, IResourceItem<?>>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
			name = "GeographyXResourceItemID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

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

	public GeographyXResourceItem(UUID geographyXResourceItemID)
	{
		this.id = geographyXResourceItemID;
	}

	@Override
	protected GeographyXResourceItemSecurityToken configureDefaultsForNewToken(GeographyXResourceItemSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}


	public java.util.UUID getId()
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

	public GeographyXResourceItem setId(java.util.UUID id)
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

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		GeographyXResourceItem that = (GeographyXResourceItem) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IGeography<?> getPrimary()
	{
		return getGeographyID();
	}

	@Override
	public IResourceItem<?> getSecondary()
	{
		return getResourceItemID();
	}
}
