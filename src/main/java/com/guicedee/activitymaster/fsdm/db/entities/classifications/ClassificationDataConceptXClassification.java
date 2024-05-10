package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationDataConceptXClassificationQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Classification", name = "ClassificationDataConceptXClassification")
@XmlRootElement
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@EqualsAndHashCode(of = "id", callSuper = false)
public class ClassificationDataConceptXClassification
		extends WarehouseClassificationRelationshipTable<ClassificationDataConcept,
		Classification,
		ClassificationDataConceptXClassification,
		ClassificationDataConceptXClassificationQueryBuilder,
		java.lang.String,
		ClassificationDataConceptXClassificationSecurityToken>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationDataConceptXClassificationID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
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
	
	public ClassificationDataConceptXClassification(java.lang.String classificationDataConceptXClassificationID)
	{
		this.id = classificationDataConceptXClassificationID;
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public ClassificationDataConceptXClassification setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public List<ClassificationDataConceptXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public ClassificationDataConceptXClassification setSecurities(List<ClassificationDataConceptXClassificationSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public ClassificationDataConcept getClassificationDataConceptID()
	{
		return this.classificationDataConceptID;
	}
	
	public ClassificationDataConceptXClassification setClassificationDataConceptID(ClassificationDataConcept classificationDataConceptID)
	{
		this.classificationDataConceptID = classificationDataConceptID;
		return this;
	}
	
	@Override
	public ClassificationDataConcept getPrimary()
	{
		return getClassificationDataConceptID();
	}
	
	@Override
	public Classification getSecondary()
	{
		return getClassificationID();
	}
}
