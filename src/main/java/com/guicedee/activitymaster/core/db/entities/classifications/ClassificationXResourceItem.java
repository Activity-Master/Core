package com.guicedee.activitymaster.core.db.entities.classifications;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.builders.ClassificationXResourceItemQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
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
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ClassificationXResourceItem")
@XmlRootElement

@Access(FIELD)
public class ClassificationXResourceItem
		extends WarehouseClassificationRelationshipTable<Classification,
						                                                ResourceItem,
						                                                ClassificationXResourceItem,
				                                                ClassificationXResourceItemQueryBuilder,
						                                                Long,
						                                                ClassificationXResourceItemSecurityToken,
				                                                IClassification<?>, IResourceItem<?>>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ClassificationXResourceItemID")
	private Long id;

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

	public ClassificationXResourceItem(Long classificationXResourceItemID)
	{
		this.id = classificationXResourceItemID;
	}

	@Override
	protected ClassificationXResourceItemSecurityToken configureDefaultsForNewToken(ClassificationXResourceItemSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
	{
		return this.id;
	}

	public List<ClassificationXResourceItemSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public Classification getClassificationID()
	{
		return this.classificationID;
	}

	public ResourceItem getResourceItemID()
	{
		return this.resourceItemID;
	}

	public ClassificationXResourceItem setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ClassificationXResourceItem setSecurities(List<ClassificationXResourceItemSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

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
