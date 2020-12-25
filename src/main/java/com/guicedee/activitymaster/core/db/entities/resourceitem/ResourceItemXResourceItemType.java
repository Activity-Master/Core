package com.guicedee.activitymaster.core.db.entities.resourceitem;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemXResourceItemTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IResourceItem;
import com.guicedee.activitymaster.core.services.dto.IResourceItemType;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;
import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
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
@Table(schema = "Resource",
       name = "ResourceItemXResourceItemType")
@XmlRootElement

@Access(FIELD)
public class ResourceItemXResourceItemType
		extends WarehouseClassificationRelationshipTypesTable<ResourceItem,
		ResourceItemType,
		ResourceItemXResourceItemType,
		ResourceItemXResourceItemTypeQueryBuilder,
		IResourceType<?>,
		java.util.UUID,
		ResourceItemXResourceItemTypeSecurityToken,
		IResourceItem<?>,
		IResourceItemType<?>>
{
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ResourceItemXResourceItemTypeID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
	@JoinColumn(name = "ResourceItemID",
	            referencedColumnName = "ResourceItemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private ResourceItem resourceItemID;
	@JoinColumn(name = "ResourceItemTypeID",
	            referencedColumnName = "ResourceItemTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private ResourceItemType resourceItemTypeID;
	
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ResourceItemXResourceItemTypeSecurityToken> securities;
	
	public ResourceItemXResourceItemType()
	{
	
	}
	
	public ResourceItemXResourceItemType(UUID resourceItemXResourceItemTypeID)
	{
		this.id = resourceItemXResourceItemTypeID;
	}
	
	@Override
	protected ResourceItemXResourceItemTypeSecurityToken configureDefaultsForNewToken(ResourceItemXResourceItemTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	public ResourceItem getResourceItemID()
	{
		return this.resourceItemID;
	}
	
	public ResourceItemType getResourceItemTypeID()
	{
		return this.resourceItemTypeID;
	}
	
	public List<ResourceItemXResourceItemTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public ResourceItemXResourceItemType setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public ResourceItemXResourceItemType setResourceItemID(ResourceItem resourceItemID)
	{
		this.resourceItemID = resourceItemID;
		return this;
	}
	
	public ResourceItemXResourceItemType setResourceItemTypeID(ResourceItemType resourceItemTypeID)
	{
		this.resourceItemTypeID = resourceItemTypeID;
		return this;
	}
	
	public ResourceItemXResourceItemType setSecurities(List<ResourceItemXResourceItemTypeSecurityToken> securities)
	{
		this.securities = securities;
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
		ResourceItemXResourceItemType that = (ResourceItemXResourceItemType) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public IResourceItem<?> getPrimary()
	{
		return getResourceItemID();
	}
	
	@Override
	public IResourceItemType<?> getSecondary()
	{
		return getResourceItemTypeID();
	}
}
