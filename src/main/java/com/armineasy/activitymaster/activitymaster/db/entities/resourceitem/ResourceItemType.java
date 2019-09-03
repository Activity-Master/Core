package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem;

import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemTypeQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsActiveFlags;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsEnterprise;
import com.armineasy.activitymaster.activitymaster.services.capabilities.INameAndDescription;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IResourceItemType;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;
import static javax.persistence.FetchType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table
@XmlRootElement

@Access(FIELD)
public class ResourceItemType
		extends WarehouseSCDNameDescriptionTable<ResourceItemType, ResourceItemTypeQueryBuilder, Long, ResourceItemTypeSecurityToken>
		implements IResourceItemType<ResourceItemType>,
				           INameAndDescription<ResourceItemType>,
				           IContainsEnterprise<ResourceItemType>,
				           IActivityMasterEntity<ResourceItemType>,
				           IContainsActiveFlags<ResourceItemType>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ResourceItemTypeID")
	private Long id;
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
	@Lob
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

	public ResourceItemType(Long resourceItemTypeID)
	{
		this.id = resourceItemTypeID;
	}

	public ResourceItemType(Long resourceItemTypeID, String resourceItemTypeName, String resourceItemTypeDesc)
	{
		this.id = resourceItemTypeID;
		this.name = resourceItemTypeName;
		this.description = resourceItemTypeDesc;
	}

	@Override
	protected ResourceItemTypeSecurityToken configureDefaultsForNewToken(ResourceItemTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public List<ResourceItemTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public List<ResourceItemXResourceItemType> getInvolvedPartyXResourceItemTypeList()
	{
		return this.involvedPartyXResourceItemTypeList;
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

	public Long getId()
	{
		return this.id;
	}

	public @NotNull @Size(min = 1,
			max = 100) String getName()
	{
		return this.name;
	}

	public @NotNull String getDescription()
	{
		return this.description;
	}

	public ResourceItemType setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ResourceItemType setName(@NotNull @Size(min = 1,
			max = 100) String name)
	{
		this.name = name;
		return this;
	}

	public ResourceItemType setDescription(@NotNull String description)
	{
		this.description = description;
		return this;
	}
}
