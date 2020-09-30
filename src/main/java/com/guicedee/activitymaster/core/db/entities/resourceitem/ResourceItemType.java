package com.guicedee.activitymaster.core.db.entities.resourceitem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IHasActiveFlags;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.INameAndDescription;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IResourceItemType;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;
import static javax.persistence.FetchType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ResourceItemType",
		schema = "Resource")
@XmlRootElement
@Access(FIELD)
public class ResourceItemType
		extends WarehouseSCDNameDescriptionTable<ResourceItemType, ResourceItemTypeQueryBuilder, Long, ResourceItemTypeSecurityToken>
		implements IResourceItemType<ResourceItemType>,
				           INameAndDescription<ResourceItemType>,
				           IContainsEnterprise<ResourceItemType>,
				           IActivityMasterEntity<ResourceItemType>,
				   IHasActiveFlags<ResourceItemType>,
				   IResourceType
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ResourceItemTypeID")
	@JsonValue
	private Long id;
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

	public ResourceItemType(Long resourceItemTypeID)
	{
		id = resourceItemTypeID;
	}

	public ResourceItemType(Long resourceItemTypeID, String resourceItemTypeName, String resourceItemTypeDesc)
	{
		id = resourceItemTypeID;
		name = resourceItemTypeName;
		description = resourceItemTypeDesc;
	}

	@Override
	protected ResourceItemTypeSecurityToken configureDefaultsForNewToken(ResourceItemTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
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
	public Long getId()
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
	public ResourceItemType setId(Long id)
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
