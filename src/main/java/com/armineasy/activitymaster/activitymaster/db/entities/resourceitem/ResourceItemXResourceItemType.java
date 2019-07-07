package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemXResourceItemTypeQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IResourceItem;
import com.armineasy.activitymaster.activitymaster.services.dto.IResourceItemType;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ResourceItemXResourceItemType")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ResourceItemXResourceItemType
		extends WarehouseRelationshipTable<ResourceItem, ResourceItemType,
				                                  ResourceItemXResourceItemType,
				                                  ResourceItemXResourceItemTypeQueryBuilder, Long,
				                                  ResourceItemXResourceItemTypeSecurityToken,
				                                  IResourceItem<?>, IResourceItemType<?>>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ResourceItemXResourceItemTypeID")
	private Long id;

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

	public ResourceItemXResourceItemType(Long resourceItemXResourceItemTypeID)
	{
		this.id = resourceItemXResourceItemTypeID;
	}

	@Override
	protected ResourceItemXResourceItemTypeSecurityToken configureDefaultsForNewToken(ResourceItemXResourceItemTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
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

	public ResourceItemXResourceItemType setId(Long id)
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
	public IResourceItem<?> getPrimary()
	{
		return getResourceItemID();
	}

	@Override
	public IResourceItemType<?> getSecondary()
	{
		return getResourceItemTypeID();
	}
}
