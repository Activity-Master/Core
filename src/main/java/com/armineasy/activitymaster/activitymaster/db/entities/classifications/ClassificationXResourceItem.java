package com.armineasy.activitymaster.activitymaster.db.entities.classifications;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders.ClassificationXResourceItemQueryBuilder;
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
@Table(name = "ClassificationXResourceItem")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ClassificationXResourceItem
		extends WarehouseClassificationRelationshipTable<Classification, ResourceItem, ClassificationXResourceItem, ClassificationXResourceItemQueryBuilder, Long, ClassificationXResourceItemSecurityToken>
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
	protected ClassificationXResourceItemSecurityToken configureDefaultsForNewToken(ClassificationXResourceItemSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "ClassificationXResourceItem(id=" + this.getId() + ", securities=" + this.getSecurities() + ", classificationID=" + this.getClassificationID() +
		       ", resourceItemID=" + this.getResourceItemID() + ")";
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof ClassificationXResourceItem))
		{
			return false;
		}
		final ClassificationXResourceItem other = (ClassificationXResourceItem) o;
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
		return other instanceof ClassificationXResourceItem;
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
