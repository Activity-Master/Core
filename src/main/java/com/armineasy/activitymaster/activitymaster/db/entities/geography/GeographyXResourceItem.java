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
import com.armineasy.activitymaster.activitymaster.services.dto.IGeography;
import com.armineasy.activitymaster.activitymaster.services.dto.IResourceItem;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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
		extends WarehouseClassificationRelationshipTable<Geography,
				                                                ResourceItem,
				                                                GeographyXResourceItem,
				                                                GeographyXResourceItemQueryBuilder,
				                                                Long,
				                                                GeographyXResourceItemSecurityToken,
				                                                IGeography<?>, IResourceItem<?>>
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
