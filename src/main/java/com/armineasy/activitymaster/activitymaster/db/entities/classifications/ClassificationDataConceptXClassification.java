package com.armineasy.activitymaster.activitymaster.db.entities.classifications;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders.ClassificationDataConceptXClassificationQueryBuilder;
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
@Table(name = "ClassificationDataConceptXClassification")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ClassificationDataConceptXClassification
		extends WarehouseClassificationRelationshipTable<ClassificationDataConcept, Classification, ClassificationDataConceptXClassification,
				                                                ClassificationDataConceptXClassificationQueryBuilder, Long
				                                                , ClassificationDataConceptXClassificationSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ClassificationDataConceptXClassificationID")
	private Long id;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXClassificationSecurityToken> securities;

	@JoinColumn(name = "ClassificationDataConceptID",
			referencedColumnName = "ClassificationDataConceptID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ClassificationDataConcept classificationDataConceptID;

	public ClassificationDataConceptXClassification()
	{

	}

	public ClassificationDataConceptXClassification(Long classificationDataConceptXClassificationID)
	{
		this.id = classificationDataConceptXClassificationID;
	}

	public String toString()
	{
		return "ClassificationDataConceptXClassification(id=" + this.getId() + ", securities=" + this.getSecurities() + ", classificationDataConceptID=" +
		       this.getClassificationDataConceptID() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public List<ClassificationDataConceptXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public ClassificationDataConcept getClassificationDataConceptID()
	{
		return this.classificationDataConceptID;
	}

	public ClassificationDataConceptXClassification setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ClassificationDataConceptXClassification setSecurities(List<ClassificationDataConceptXClassificationSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public ClassificationDataConceptXClassification setClassificationDataConceptID(ClassificationDataConcept classificationDataConceptID)
	{
		this.classificationDataConceptID = classificationDataConceptID;
		return this;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof ClassificationDataConceptXClassification))
		{
			return false;
		}
		final ClassificationDataConceptXClassification other = (ClassificationDataConceptXClassification) o;
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
		return other instanceof ClassificationDataConceptXClassification;
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
