package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemXClassificationQueryBuilder;
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
@Table(name = "ResourceItemXClassification")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ResourceItemXClassification
		extends WarehouseClassificationRelationshipTable<ResourceItem, Classification, ResourceItemXClassification, ResourceItemXClassificationQueryBuilder, Long, ResourceItemXClassificationSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ResourceItemXClassificationID")
	private Long id;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ResourceItemXClassificationSecurityToken> securities;

	@JoinColumn(name = "ResourceItemID",
			referencedColumnName = "ResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ResourceItem resourceItemID;

	public ResourceItemXClassification()
	{

	}

	public ResourceItemXClassification(Long resourceItemXClassificationID)
	{
		this.id = resourceItemXClassificationID;
	}

	@Override
	protected ResourceItemXClassificationSecurityToken configureDefaultsForNewToken(ResourceItemXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "ResourceItemXClassification(id=" + this.getId() + ", securities=" + this.getSecurities() + ", resourceItemID=" + this.getResourceItemID() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public List<ResourceItemXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public ResourceItem getResourceItemID()
	{
		return this.resourceItemID;
	}

	public ResourceItemXClassification setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ResourceItemXClassification setSecurities(List<ResourceItemXClassificationSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public ResourceItemXClassification setResourceItemID(ResourceItem resourceItemID)
	{
		this.resourceItemID = resourceItemID;
		return this;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof ResourceItemXClassification))
		{
			return false;
		}
		final ResourceItemXClassification other = (ResourceItemXClassification) o;
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
		return other instanceof ResourceItemXClassification;
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
