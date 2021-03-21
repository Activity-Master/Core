package com.guicedee.activitymaster.core.db.entities.resourceitem;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.client.services.builders.warehouse.resourceitem.IResourceItemType;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemTypeQueryBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;
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
@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ResourceItemType
		extends WarehouseSCDNameDescriptionTable<ResourceItemType, ResourceItemTypeQueryBuilder, java.util.UUID>
		implements IResourceItemType<ResourceItemType, ResourceItemTypeQueryBuilder>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ResourceItemTypeID")
	@JsonValue
	@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
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
			fetch = FetchType.LAZY)
	private List<ResourceItemTypeSecurityToken> securities;
	
	@OneToMany(
			mappedBy = "resourceItemTypeID",
			fetch = FetchType.LAZY)
	private List<ResourceItemXResourceItemType> involvedPartyXResourceItemTypeList;
	
	public ResourceItemType()
	{
	
	}
	
	public ResourceItemType(UUID resourceItemTypeID)
	{
		id = resourceItemTypeID;
	}
	
	public ResourceItemType(UUID resourceItemTypeID, String resourceItemTypeName, String resourceItemTypeDesc)
	{
		id = resourceItemTypeID;
		name = resourceItemTypeName;
		description = resourceItemTypeDesc;
	}
	
	public List<ResourceItemTypeSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public List<ResourceItemXResourceItemType> getInvolvedPartyXResourceItemTypeList()
	{
		return involvedPartyXResourceItemTypeList;
	}
	
	public ResourceItemType setSecurities(List<ResourceItemTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
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
	public java.util.UUID getId()
	{
		return id;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public @NotNull String getDescription()
	{
		return description;
	}
	
	@Override
	public ResourceItemType setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public ResourceItemType setName(String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	public ResourceItemType setDescription(@NotNull String description)
	{
		this.description = description;
		return this;
	}
}
