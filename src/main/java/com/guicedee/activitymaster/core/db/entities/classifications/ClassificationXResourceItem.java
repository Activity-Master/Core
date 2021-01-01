package com.guicedee.activitymaster.core.db.entities.classifications;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.builders.ClassificationXResourceItemQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IResourceItem;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Classification",
		name = "ClassificationXResourceItem")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
public class ClassificationXResourceItem
		extends WarehouseClassificationRelationshipTable<Classification,
				                                                ResourceItem,
				                                                ClassificationXResourceItem,
				                                                ClassificationXResourceItemQueryBuilder,
				                                                java.util.UUID,
				                                                ClassificationXResourceItemSecurityToken,
				                                                IClassification<?>, IResourceItem<?>>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "ClassificationXResourceItemID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ClassificationXResourceItemSecurityToken> securities;

	@JoinColumn(name = "ClassificationID",
			referencedColumnName = "ClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Classification classificationID;

	@JoinColumn(name = "ResourceItemID",
			referencedColumnName = "ResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ResourceItem resourceItemID;

	public ClassificationXResourceItem()
	{

	}

	public ClassificationXResourceItem(UUID classificationXResourceItemID)
	{
		id = classificationXResourceItemID;
	}

	@Override
	protected ClassificationXResourceItemSecurityToken configureDefaultsForNewToken(ClassificationXResourceItemSecurityToken stAdmin,  ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public java.util.UUID getId()
	{
		return id;
	}

	public List<ClassificationXResourceItemSecurityToken> getSecurities()
	{
		return securities;
	}

	@Override
	public Classification getClassificationID()
	{
		return classificationID;
	}

	public ResourceItem getResourceItemID()
	{
		return resourceItemID;
	}

	@Override
	public ClassificationXResourceItem setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public ClassificationXResourceItem setSecurities(List<ClassificationXResourceItemSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	@Override
	public ClassificationXResourceItem setClassificationID(Classification classificationID)
	{
		this.classificationID = classificationID;
		return this;
	}

	public ClassificationXResourceItem setResourceItemID(ResourceItem resourceItemID)
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
		ClassificationXResourceItem that = (ClassificationXResourceItem) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IClassification<?> getPrimary()
	{
		return getClassificationID();
	}

	@Override
	public IResourceItem<?> getSecondary()
	{
		return getResourceItemID();
	}
}
