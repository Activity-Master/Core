package com.guicedee.activitymaster.fsdm.db.entities.resourceitem;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemXResourceItemTypeQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
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
       name = "ResourceItemXResourceItemType")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ResourceItemXResourceItemType
		extends WarehouseClassificationRelationshipTypesTable<ResourceItem,
		ResourceItemType,
		ResourceItemXResourceItemType,
		ResourceItemXResourceItemTypeQueryBuilder,
		UUID>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ResourceItemXResourceItemTypeID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
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
	
	public UUID getId()
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
	
	public ResourceItemXResourceItemType setId(UUID id)
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
	public ResourceItem getPrimary()
	{
		return getResourceItemID();
	}
	
	@Override
	public ResourceItemType getSecondary()
	{
		return getResourceItemTypeID();
	}
}
