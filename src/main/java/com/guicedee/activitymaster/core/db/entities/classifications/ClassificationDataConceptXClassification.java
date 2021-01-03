package com.guicedee.activitymaster.core.db.entities.classifications;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.builders.ClassificationDataConceptXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IClassificationDataConcept;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Classification", name = "ClassificationDataConceptXClassification")
@XmlRootElement
@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ClassificationDataConceptXClassification
		extends WarehouseClassificationRelationshipTable<ClassificationDataConcept,
		Classification,
		ClassificationDataConceptXClassification,
		ClassificationDataConceptXClassificationQueryBuilder,
		java.util.UUID,
		ClassificationDataConceptXClassificationSecurityToken,
		IClassificationDataConcept<?>, IClassification<?>>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationDataConceptXClassificationID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
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
	
	public ClassificationDataConceptXClassification(UUID classificationDataConceptXClassificationID)
	{
		this.id = classificationDataConceptXClassificationID;
	}
	
	public java.util.UUID getId()
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
	
	public ClassificationDataConceptXClassification setId(java.util.UUID id)
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
