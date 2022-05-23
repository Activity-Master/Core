package com.guicedee.activitymaster.fsdm.db.entities.resourceitem;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemXResourceItemTypeQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Resource",
       name = "ResourceItemXResourceItemType")
@XmlRootElement

@Access(AccessType.FIELD)
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
		java.lang.String>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ResourceItemXResourceItemTypeID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
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
	
	public ResourceItemXResourceItemType(java.lang.String resourceItemXResourceItemTypeID)
	{
		this.id = resourceItemXResourceItemTypeID;
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public ResourceItemXResourceItemType setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ResourceItem getResourceItemID()
	{
		return this.resourceItemID;
	}
	
	public ResourceItemXResourceItemType setResourceItemID(ResourceItem resourceItemID)
	{
		this.resourceItemID = resourceItemID;
		return this;
	}
	
	public ResourceItemType getResourceItemTypeID()
	{
		return this.resourceItemTypeID;
	}
	
	public ResourceItemXResourceItemType setResourceItemTypeID(ResourceItemType resourceItemTypeID)
	{
		this.resourceItemTypeID = resourceItemTypeID;
		return this;
	}
	
	public List<ResourceItemXResourceItemTypeSecurityToken> getSecurities()
	{
		return this.securities;
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
