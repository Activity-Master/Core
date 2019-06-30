package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemDataXClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ResourceItemDataXClassification")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ResourceItemDataXClassification
		extends WarehouseClassificationRelationshipTable<ResourceItemData, Classification, ResourceItemDataXClassification, ResourceItemDataXClassificationQueryBuilder, Long, ResourceItemDataXClassificationSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ResourceItemDataXClassificationID")
	private Long id;

	@JoinColumn(name = "ResourceItemDataID",
			referencedColumnName = "ResourceItemDataID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ResourceItemData resourceItemDataID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ResourceItemDataXClassificationSecurityToken> securities;

	public ResourceItemDataXClassification()
	{

	}

	public ResourceItemDataXClassification(Long resourceItemDataXClassificationID)
	{
		this.id = resourceItemDataXClassificationID;
	}

	@Override
	protected ResourceItemDataXClassificationSecurityToken configureDefaultsForNewToken(ResourceItemDataXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "ResourceItemDataXClassification(id=" + this.getId() + ", resourceItemDataID=" + this.getResourceItemDataID() + ", securities=" + this.getSecurities() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public ResourceItemData getResourceItemDataID()
	{
		return this.resourceItemDataID;
	}

	public List<ResourceItemDataXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public ResourceItemDataXClassification setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ResourceItemDataXClassification setResourceItemDataID(ResourceItemData resourceItemDataID)
	{
		this.resourceItemDataID = resourceItemDataID;
		return this;
	}

	public ResourceItemDataXClassification setSecurities(List<ResourceItemDataXClassificationSecurityToken> securities)
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
		if (!(o instanceof ResourceItemDataXClassification))
		{
			return false;
		}
		final ResourceItemDataXClassification other = (ResourceItemDataXClassification) o;
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
		return other instanceof ResourceItemDataXClassification;
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
