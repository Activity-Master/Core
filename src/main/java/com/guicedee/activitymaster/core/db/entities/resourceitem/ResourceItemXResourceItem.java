package com.guicedee.activitymaster.core.db.entities.resourceitem;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemXResourceItemQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IResourceItem;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
@Table(schema = "Resource",
       name = "ResourceItemXResourceItem")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
public class ResourceItemXResourceItem
		extends WarehouseClassificationRelationshipTable<ResourceItem,
		ResourceItem,
		ResourceItemXResourceItem,
		ResourceItemXResourceItemQueryBuilder,
		java.util.UUID,
		ResourceItemXResourceItemSecurityToken,
		IResourceItem<?>, IResourceItem<?>>
		implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ResourceItemXResourceItemID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ResourceItemXResourceItemSecurityToken> securities;
	
	@JoinColumn(name = "ChildResourceItemID",
	            referencedColumnName = "ResourceItemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private ResourceItem childResourceItemID;
	@JoinColumn(name = "ParentResourceItemID",
	            referencedColumnName = "ResourceItemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private ResourceItem parentResourceItemID;
	
	public ResourceItemXResourceItem()
	{
	
	}
	
	public ResourceItemXResourceItem(UUID productXResourceItemID)
	{
		id = productXResourceItemID;
	}
	
	@Override
	protected ResourceItemXResourceItemSecurityToken configureDefaultsForNewToken(ResourceItemXResourceItemSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	@Override
	public java.util.UUID getId()
	{
		return id;
	}
	
	public List<ResourceItemXResourceItemSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public ResourceItem getChildResourceItemID()
	{
		return childResourceItemID;
	}
	
	public ResourceItem getParentResourceItemID()
	{
		return parentResourceItemID;
	}
	
	@Override
	public ResourceItemXResourceItem setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public ResourceItemXResourceItem setSecurities(List<ResourceItemXResourceItemSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public ResourceItemXResourceItem setChildResourceItemID(ResourceItem childResourceItemID)
	{
		this.childResourceItemID = childResourceItemID;
		return this;
	}
	
	public ResourceItemXResourceItem setParentResourceItemID(ResourceItem parentResourceItemID)
	{
		this.parentResourceItemID = parentResourceItemID;
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
		ResourceItemXResourceItem that = (ResourceItemXResourceItem) o;
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
		return getParentResourceItemID();
	}
	
	@Override
	public IResourceItem<?> getSecondary()
	{
		return getChildResourceItemID();
	}
}
