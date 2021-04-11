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
		UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ResourceItemXResourceItemID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
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
	public UUID getId()
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
	public ResourceItemXResourceItem setId(UUID id)
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
