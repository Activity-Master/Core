package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemXResourceItemTypeQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

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
				                                  ResourceItemXResourceItemTypeSecurityToken>
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

	public String toString()
	{
		return "ResourceItemXResourceItemType(id=" + this.getId() + ", resourceItemID=" + this.getResourceItemID() + ", resourceItemTypeID=" + this.getResourceItemTypeID() +
		       ", securities=" + this.getSecurities() + ")";
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof ResourceItemXResourceItemType))
		{
			return false;
		}
		final ResourceItemXResourceItemType other = (ResourceItemXResourceItemType) o;
		if (!other.canEqual((Object) this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
		{
			return false;
		}
		return true;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof ResourceItemXResourceItemType;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}
