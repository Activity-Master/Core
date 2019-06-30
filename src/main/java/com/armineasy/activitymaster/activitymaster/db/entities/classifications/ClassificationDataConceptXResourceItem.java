package com.armineasy.activitymaster.activitymaster.db.entities.classifications;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders.ClassificationDataConceptXResourceItemQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
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
@Table(name = "ClassificationDataConceptXResourceItem")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ClassificationDataConceptXResourceItem
		extends WarehouseClassificationRelationshipTable<ClassificationDataConcept, ResourceItem, ClassificationDataConceptXResourceItem, ClassificationDataConceptXResourceItemQueryBuilder
						                                  , Long, ClassificationDataConceptXResourceItemSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ClassificationDataConceptXResourceItemID")
	private Long id;

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

	public ClassificationDataConceptXResourceItem(Long classificationDataConceptXResourceItemID)
	{
		this.id = classificationDataConceptXResourceItemID;
	}

	@Override
	protected ClassificationDataConceptXResourceItemSecurityToken configureDefaultsForNewToken(ClassificationDataConceptXResourceItemSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "ClassificationDataConceptXResourceItem(id=" + this.getId() + ", classificationDataConceptID=" + this.getClassificationDataConceptID() + ", resourceItemID=" +
		       this.getResourceItemID() + ", securities=" + this.getSecurities() + ")";
	}

	public Long getId()
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

	public ClassificationDataConceptXResourceItem setId(Long id)
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof ClassificationDataConceptXResourceItem))
		{
			return false;
		}
		final ClassificationDataConceptXResourceItem other = (ClassificationDataConceptXResourceItem) o;
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
		return other instanceof ClassificationDataConceptXResourceItem;
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
