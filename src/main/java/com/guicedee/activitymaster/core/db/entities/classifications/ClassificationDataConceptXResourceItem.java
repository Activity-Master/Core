package com.guicedee.activitymaster.core.db.entities.classifications;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.builders.ClassificationDataConceptXResourceItemQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.dto.IClassificationDataConcept;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IResourceItem;
import com.guicedee.activitymaster.core.services.dto.ISystems;

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
@Table(schema="Classification",name = "ClassificationDataConceptXResourceItem")
@XmlRootElement

@Access(FIELD)
public class ClassificationDataConceptXResourceItem
		extends WarehouseClassificationRelationshipTable<ClassificationDataConcept,
						                                                ResourceItem,
						                                                ClassificationDataConceptXResourceItem,
				                                                ClassificationDataConceptXResourceItemQueryBuilder,
						                                                java.util.UUID,
						                                                ClassificationDataConceptXResourceItemSecurityToken,
				                                                IClassificationDataConcept<?>, IResourceItem<?>>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
			name = "ClassificationDataConceptXResourceItemID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@JoinColumn(name = "ClassificationDataConceptID",
			referencedColumnName = "ClassificationDataConceptID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ClassificationDataConcept classificationDataConceptID;


	@JoinColumn(name = "ResourceItemID",
			referencedColumnName = "ResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ResourceItem resourceItemID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXResourceItemSecurityToken> securities;

	public ClassificationDataConceptXResourceItem()
	{

	}

	public ClassificationDataConceptXResourceItem(UUID classificationDataConceptXResourceItemID)
	{
		this.id = classificationDataConceptXResourceItemID;
	}

	@Override
	protected ClassificationDataConceptXResourceItemSecurityToken configureDefaultsForNewToken(ClassificationDataConceptXResourceItemSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public ClassificationDataConcept getClassificationDataConceptID()
	{
		return this.classificationDataConceptID;
	}

	public ResourceItem getResourceItemID()
	{
		return this.resourceItemID;
	}

	public List<ClassificationDataConceptXResourceItemSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public ClassificationDataConceptXResourceItem setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public ClassificationDataConceptXResourceItem setClassificationDataConceptID(ClassificationDataConcept classificationDataConceptID)
	{
		this.classificationDataConceptID = classificationDataConceptID;
		return this;
	}

	public ClassificationDataConceptXResourceItem setResourceItemID(ResourceItem resourceItemID)
	{
		this.resourceItemID = resourceItemID;
		return this;
	}

	public ClassificationDataConceptXResourceItem setSecurities(List<ClassificationDataConceptXResourceItemSecurityToken> securities)
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
		ClassificationDataConceptXResourceItem that = (ClassificationDataConceptXResourceItem) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IClassificationDataConcept<?> getPrimary()
	{
		return getClassificationDataConceptID();
	}

	@Override
	public IResourceItem<?> getSecondary()
	{
		return getResourceItemID();
	}
}
