package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationDataConceptXResourceItemQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
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
@Getter
@Entity
@Table(schema = "Classification", name = "ClassificationDataConceptXResourceItem")
@XmlRootElement
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@EqualsAndHashCode(of = "id", callSuper = false)
public class ClassificationDataConceptXResourceItem
		extends WarehouseClassificationRelationshipTable<ClassificationDataConcept,
		ResourceItem,
		ClassificationDataConceptXResourceItem,
		ClassificationDataConceptXResourceItemQueryBuilder,
		java.lang.String,
		ClassificationDataConceptXResourceItemSecurityToken>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationDataConceptXResourceItemID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
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
	
	public ClassificationDataConceptXResourceItem(java.lang.String classificationDataConceptXResourceItemID)
	{
		this.id = classificationDataConceptXResourceItemID;
	}
	
	public ClassificationDataConceptXResourceItem setId(java.lang.String id)
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
	public ClassificationDataConcept getPrimary()
	{
		return getClassificationDataConceptID();
	}
	
	@Override
	public ResourceItem getSecondary()
	{
		return getResourceItemID();
	}
}
