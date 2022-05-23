package com.guicedee.activitymaster.fsdm.db.entities.resourceitem;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemXResourceItemQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

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
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ResourceItemXResourceItem
		extends WarehouseClassificationRelationshipTable<ResourceItem,
		ResourceItem,
		ResourceItemXResourceItem,
		ResourceItemXResourceItemQueryBuilder,
		java.lang.String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ResourceItemXResourceItemID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
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
	
	public ResourceItemXResourceItem(java.lang.String productXResourceItemID)
	{
		id = productXResourceItemID;
	}
	
	@Override
	public java.lang.String getId()
	{
		return id;
	}
	
	@Override
	public ResourceItemXResourceItem setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public List<ResourceItemXResourceItemSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public ResourceItemXResourceItem setSecurities(List<ResourceItemXResourceItemSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public ResourceItem getChildResourceItemID()
	{
		return childResourceItemID;
	}
	
	public ResourceItemXResourceItem setChildResourceItemID(ResourceItem childResourceItemID)
	{
		this.childResourceItemID = childResourceItemID;
		return this;
	}
	
	public ResourceItem getParentResourceItemID()
	{
		return parentResourceItemID;
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
	public ResourceItem getPrimary()
	{
		return getParentResourceItemID();
	}
	
	@Override
	public ResourceItem getSecondary()
	{
		return getChildResourceItemID();
	}
}
