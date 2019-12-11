package com.guicedee.activitymaster.core.db.entities.resourceitem;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IResourceItem;
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
@Table(schema="Resource",name = "ResourceItemXClassification")
@XmlRootElement

@Access(FIELD)
public class ResourceItemXClassification
		extends WarehouseClassificationRelationshipTable<ResourceItem,
				                                                Classification,
						                                                ResourceItemXClassification,
				                                                ResourceItemXClassificationQueryBuilder,
						                                                Long,
						                                                ResourceItemXClassificationSecurityToken,
				                                                IResourceItem<?>, IClassification<?>>
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
	protected ResourceItemXClassificationSecurityToken configureDefaultsForNewToken(ResourceItemXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
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
		ResourceItemXClassification that = (ResourceItemXClassification) o;
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
	public IClassification<?> getSecondary()
	{
		return getClassificationID();
	}
}
