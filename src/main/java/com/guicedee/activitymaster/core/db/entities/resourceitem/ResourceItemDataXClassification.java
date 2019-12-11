package com.guicedee.activitymaster.core.db.entities.resourceitem;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemDataXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IResourceData;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Resource",name = "ResourceItemDataXClassification")
@XmlRootElement

@Access(FIELD)
public class ResourceItemDataXClassification
		extends WarehouseClassificationRelationshipTable<ResourceItemData,
				                                                Classification,
						                                                ResourceItemDataXClassification,
				                                                ResourceItemDataXClassificationQueryBuilder,
						                                                Long,
						                                                ResourceItemDataXClassificationSecurityToken,
				                                                IResourceData<?>, IClassification<?>>
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
	protected ResourceItemDataXClassificationSecurityToken configureDefaultsForNewToken(ResourceItemDataXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
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
		ResourceItemDataXClassification that = (ResourceItemDataXClassification) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IResourceData<?> getPrimary()
	{
		return getResourceItemDataID();
	}

	@Override
	public IClassification<?> getSecondary()
	{
		return getClassificationID();
	}
}
