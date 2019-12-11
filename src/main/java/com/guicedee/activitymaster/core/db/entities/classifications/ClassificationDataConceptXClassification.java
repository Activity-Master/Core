package com.guicedee.activitymaster.core.db.entities.classifications;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.builders.ClassificationDataConceptXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IClassificationDataConcept;

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
@Table(schema="Classification",name = "ClassificationDataConceptXClassification")
@XmlRootElement

@Access(FIELD)
public class ClassificationDataConceptXClassification
		extends WarehouseClassificationRelationshipTable<ClassificationDataConcept,
						                                                Classification,
						                                                ClassificationDataConceptXClassification,
				                                                ClassificationDataConceptXClassificationQueryBuilder,
						                                                Long,
						                                                ClassificationDataConceptXClassificationSecurityToken,
				                                                IClassificationDataConcept<?>, IClassification<?>>
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
		ClassificationDataConceptXClassification that = (ClassificationDataConceptXClassification) o;
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
	public IClassification<?> getSecondary()
	{
		return getClassificationID();
	}
}
