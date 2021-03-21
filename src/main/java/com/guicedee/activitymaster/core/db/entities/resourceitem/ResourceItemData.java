package com.guicedee.activitymaster.core.db.entities.resourceitem;

import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.client.services.builders.warehouse.resourceitem.IResourceData;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemDataQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.*;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Resource",
		name = "ResourceItemData")
@XmlRootElement
@Access(FIELD)
public class ResourceItemData
		extends WarehouseTable<ResourceItemData, ResourceItemDataQueryBuilder, java.util.UUID>
		implements IResourceData<ResourceItemData,ResourceItemDataQueryBuilder>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ResourceItemDataID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
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
	
	public ResourceItemData(UUID resourceItemDataID)
	{
		id = resourceItemDataID;
	}
	
	public List<ResourceItemDataSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public List<ResourceItemDataXClassification> getClassifications()
	{
		return classifications;
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
	
	@Override
	public java.util.UUID getId()
	{
		return id;
	}
	
	public byte[] getResourceItemData()
	{
		return resourceItemData;
	}
	
	public ResourceItem getResource()
	{
		return resource;
	}
	
	@Override
	public ResourceItemData setId(java.util.UUID id)
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
	
	@Override
	public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable, IClassification<?,?> classificationValue, ISystems<?,?> system)
	{
	
	}
}