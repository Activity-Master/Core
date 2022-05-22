package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationDataConceptXResourceItemQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
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
public class ClassificationDataConceptXResourceItem
		extends WarehouseClassificationRelationshipTable<ClassificationDataConcept,
		ResourceItem,
		ClassificationDataConceptXResourceItem,
		ClassificationDataConceptXResourceItemQueryBuilder,
		UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationDataConceptXResourceItemID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
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
	
	public ClassificationDataConceptXResourceItem(UUID classificationDataConceptXResourceItemID)
	{
		this.id = classificationDataConceptXResourceItemID;
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public ClassificationDataConceptXResourceItem setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public ClassificationDataConcept getClassificationDataConceptID()
	{
		return this.classificationDataConceptID;
	}
	
	public ClassificationDataConceptXResourceItem setClassificationDataConceptID(ClassificationDataConcept classificationDataConceptID)
	{
		this.classificationDataConceptID = classificationDataConceptID;
		return this;
	}
	
	public ResourceItem getResourceItemID()
	{
		return this.resourceItemID;
	}
	
	public ClassificationDataConceptXResourceItem setResourceItemID(ResourceItem resourceItemID)
	{
		this.resourceItemID = resourceItemID;
		return this;
	}
	
	public List<ClassificationDataConceptXResourceItemSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public ClassificationDataConceptXResourceItem setSecurities(List<ClassificationDataConceptXResourceItemSecurityToken> securities)
	{
		this.securities = securities;
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
		ClassificationDataConceptXResourceItem that = (ClassificationDataConceptXResourceItem) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
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
