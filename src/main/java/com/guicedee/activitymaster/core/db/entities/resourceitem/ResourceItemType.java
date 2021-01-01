package com.guicedee.activitymaster.core.db.entities.resourceitem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsActiveFlags;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.IContainsNameAndDescription;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IResourceItemType;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
public class ResourceItemType
		extends WarehouseSCDNameDescriptionTable<ResourceItemType, ResourceItemTypeQueryBuilder, java.util.UUID, ResourceItemTypeSecurityToken>
		implements IResourceItemType<ResourceItemType>,
		           IContainsNameAndDescription<ResourceItemType>,
				           IContainsEnterprise<ResourceItemType>,
				           IActivityMasterEntity<ResourceItemType>,
				   IContainsActiveFlags<ResourceItemType>,
				   IResourceType
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "ResourceItemTypeID")
	@JsonValue@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	@Basic(optional = false,
			fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 100)
	@Column(nullable = false,
			length = 100,
			name = "ResourceItemTypeName")
	@JsonIgnore
	private String name;
	@Basic(optional = false,
			fetch = EAGER)
	@NotNull
	@Column(nullable = false,
			name = "ResourceItemTypeDesc")
	@JsonIgnore
	private String description;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ResourceItemTypeSecurityToken> securities;

	@OneToMany(
			mappedBy = "resourceItemTypeID",
			fetch = FetchType.LAZY)
	@JsonIgnore
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

	@Override
	protected ResourceItemTypeSecurityToken configureDefaultsForNewToken(ResourceItemTypeSecurityToken stAdmin,  ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
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
		return "ResourceType - " + getName();
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
	
	@Override
	public String name()
	{
		return getName();
	}
	
	@Override
	public String classificationName()
	{
		return getName();
	}
	
	@Override
	public String classificationValue()
	{
		return getName();
	}
	
	@Override
	public String classificationDescription()
	{
		return getDescription();
	}
}
