package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemDataQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IResourceData;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ResourceItemData")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ResourceItemData
		extends WarehouseTable<ResourceItemData, ResourceItemDataQueryBuilder, Long, ResourceItemDataSecurityToken>
		implements IContainsClassifications<ResourceItemData, Classification, ResourceItemDataXClassification, IResourceItemClassification<?>, IResourceData<?>, IClassification<?>, ResourceItemData>
				           , IResourceData<ResourceItemData>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ResourceItemDataID")
	private Long id;
	@Lob()
	@Column(nullable = false,
			name = "ResourceItemData")
	private byte[] resourceItemData;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ResourceItemDataSecurityToken> securities;

	@JoinColumn(name = "ResourceItemID",
			referencedColumnName = "ResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ResourceItem resource;

	@OneToMany(
			mappedBy = "resourceItemDataID",
			fetch = FetchType.LAZY)
	private List<ResourceItemDataXClassification> classifications;

	public ResourceItemData()
	{

	}

	public ResourceItemData(Long resourceItemDataID)
	{
		this.id = resourceItemDataID;
	}

	@Override
	protected ResourceItemDataSecurityToken configureDefaultsForNewToken(ResourceItemDataSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public void configureForClassification(ResourceItemDataXClassification classificationLink, IEnterprise<?> enterprise)
	{
		classificationLink.setResourceItemDataID(this);
	}

	public List<ResourceItemDataSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public List<ResourceItemDataXClassification> getClassifications()
	{
		return this.classifications;
	}

	public ResourceItemData setSecurities(List<ResourceItemDataSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public ResourceItemData setClassifications(List<ResourceItemDataXClassification> classifications)
	{
		this.classifications = classifications;
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
		ResourceItemData itemData = (ResourceItemData) o;
		return Objects.equals(getId(), itemData.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public String toString()
	{
		return "Data - " + getId();
	}

	public Long getId()
	{
		return this.id;
	}

	public byte[] getResourceItemData()
	{
		return this.resourceItemData;
	}

	public ResourceItem getResource()
	{
		return this.resource;
	}

	public ResourceItemData setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ResourceItemData setResourceItemData(byte[] resourceItemData)
	{
		this.resourceItemData = resourceItemData;
		return this;
	}

	public ResourceItemData setResource(ResourceItem resource)
	{
		this.resource = resource;
		return this;
	}
}
