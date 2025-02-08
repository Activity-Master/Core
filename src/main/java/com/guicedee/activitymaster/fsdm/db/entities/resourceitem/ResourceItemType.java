package com.guicedee.activitymaster.fsdm.db.entities.resourceitem;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseNameAndDescriptionTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItemType;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemTypeQueryBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.FetchType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ResourceItemType",
       schema = "Resource")
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
public class ResourceItemType
		extends WarehouseSCDTable<ResourceItemType, ResourceItemTypeQueryBuilder, String, ResourceItemTypeSecurityToken>
		implements IResourceItemType<ResourceItemType, ResourceItemTypeQueryBuilder>,
		           IWarehouseNameAndDescriptionTable<ResourceItemType,ResourceItemTypeQueryBuilder,String>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ResourceItemTypeID")
	@JsonValue
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 100)
	@Column(nullable = false,
	        length = 100,
	        name = "ResourceItemTypeName")
	private String name;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Column(nullable = false,
	        name = "ResourceItemTypeDesc")
	private String description;
	
@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<ResourceItemTypeSecurityToken> securities;
	
@OneToMany(
			mappedBy = "resourceItemTypeID",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<ResourceItemXResourceItemType> involvedPartyXResourceItemTypeList;
	
	public ResourceItemType()
	{
	
	}
	
	public ResourceItemType(java.lang.String resourceItemTypeID)
	{
		id = resourceItemTypeID;
	}
	
	public ResourceItemType(java.lang.String resourceItemTypeID, String resourceItemTypeName, String resourceItemTypeDesc)
	{
		id = resourceItemTypeID;
		name = resourceItemTypeName;
		description = resourceItemTypeDesc;
	}
	
	@Override
	public void configureSecurityEntity(ResourceItemTypeSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	public List<ResourceItemTypeSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public ResourceItemType setSecurities(List<ResourceItemTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public List<ResourceItemXResourceItemType> getInvolvedPartyXResourceItemTypeList()
	{
		return involvedPartyXResourceItemTypeList;
	}
	
	public ResourceItemType setInvolvedPartyXResourceItemTypeList(List<ResourceItemXResourceItemType> involvedPartyXResourceItemTypeList)
	{
		this.involvedPartyXResourceItemTypeList = involvedPartyXResourceItemTypeList;
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
		ResourceItemType that = (ResourceItemType) o;
		return Objects.equals(getName(), that.getName());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	@Override
	public java.lang.String getId()
	{
		return id;
	}
	
	@Override
	public ResourceItemType setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public ResourceItemType setName(String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	public @NotNull String getDescription()
	{
		return description;
	}
	
	@Override
	public ResourceItemType setDescription(@NotNull String description)
	{
		this.description = description;
		return this;
	}
}
