package com.armineasy.activitymaster.activitymaster.db.entities.classifications;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders.ClassificationXClassificationQueryBuilder;
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
@Table(name = "ClassificationXClassification")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ClassificationXClassification
		extends WarehouseClassificationRelationshipTable<Classification, Classification, ClassificationXClassification, ClassificationXClassificationQueryBuilder, Long, ClassificationXClassificationSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ClassificationXClassificationID")
	private Long id;

	@JoinColumn(name = "ChildClassificationID",
			referencedColumnName = "ClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Classification childClassificationID;

	@JoinColumn(name = "ParentClassificationID",
			referencedColumnName = "ClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Classification parentClassificationID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ClassificationXClassificationSecurityToken> securities;

	public ClassificationXClassification()
	{

	}

	public ClassificationXClassification(Long classificationXClassificationID)
	{
		this.id = classificationXClassificationID;
	}

	@Override
	protected ClassificationXClassificationSecurityToken configureDefaultsForNewToken(ClassificationXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "ClassificationXClassification(id=" + this.getId() + ", childClassificationID=" + this.getChildClassificationID() + ", parentClassificationID=" +
		       this.getParentClassificationID() + ", securities=" + this.getSecurities() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public Classification getChildClassificationID()
	{
		return this.childClassificationID;
	}

	public Classification getParentClassificationID()
	{
		return this.parentClassificationID;
	}

	public List<ClassificationXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public ClassificationXClassification setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ClassificationXClassification setChildClassificationID(Classification childClassificationID)
	{
		this.childClassificationID = childClassificationID;
		return this;
	}

	public ClassificationXClassification setParentClassificationID(Classification parentClassificationID)
	{
		this.parentClassificationID = parentClassificationID;
		return this;
	}

	public ClassificationXClassification setSecurities(List<ClassificationXClassificationSecurityToken> securities)
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
		if (!(o instanceof ClassificationXClassification))
		{
			return false;
		}
		final ClassificationXClassification other = (ClassificationXClassification) o;
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
		return other instanceof ClassificationXClassification;
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
