package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationXClassificationQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;

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
@Table(schema = "Classification",
       name = "ClassificationXClassification")
@XmlRootElement
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@EqualsAndHashCode(of="id",callSuper = false)
public class ClassificationXClassification
		extends WarehouseClassificationRelationshipTable<Classification,
		Classification,
		ClassificationXClassification,
		ClassificationXClassificationQueryBuilder,
		java.lang.String,
		ClassificationXClassificationSecurityToken>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationXClassificationID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@Getter
	@JoinColumn(name = "ChildClassificationID",
	            referencedColumnName = "ClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Classification childClassificationID;
	
	@Getter
	@JoinColumn(name = "ParentClassificationID",
	            referencedColumnName = "ClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Classification parentClassificationID;
	
	@Getter
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ClassificationXClassificationSecurityToken> securities;
	
	public ClassificationXClassification()
	{
	
	}
	
	public ClassificationXClassification(java.lang.String classificationXClassificationID)
	{
		id = classificationXClassificationID;
	}
	
	public ClassificationXClassification setSecurities(List<ClassificationXClassificationSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	@Override
	public java.lang.String getId()
	{
		return id;
	}
	
	@Override
	public ClassificationXClassification setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public Classification getPrimary()
	{
		return getParentClassificationID();
	}
	
	public ClassificationXClassification setParentClassificationID(Classification parentClassificationID)
	{
		this.parentClassificationID = parentClassificationID;
		return this;
	}
	
	@Override
	public Classification getSecondary()
	{
		return getChildClassificationID();
	}
	
	public ClassificationXClassification setChildClassificationID(Classification childClassificationID)
	{
		this.childClassificationID = childClassificationID;
		return this;
	}
}
