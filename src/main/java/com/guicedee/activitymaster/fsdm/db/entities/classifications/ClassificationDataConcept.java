package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationDataConceptQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.FetchType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings({"unused", "rawtypes"})
@Entity
@Table(schema = "Classification",
       name = "ClassificationDataConcept")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ClassificationDataConcept
		extends WarehouseSCDTable<ClassificationDataConcept, ClassificationDataConceptQueryBuilder, String,ClassificationDataConceptSecurityToken>
		implements IClassificationDataConcept<ClassificationDataConcept, ClassificationDataConceptQueryBuilder>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationDataConceptID")
	@JsonValue
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 100)
	@Column(nullable = false,
	        length = 100,
	        name = "classificationDataConceptName")
	private String name;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 1500)
	@Column(nullable = false,
	        length = 1500,
	        name = "ClassificationDataConceptDesc")
	private String description;
	
	@OneToMany(
			mappedBy = "concept",
			fetch = FetchType.LAZY)
	
	private List<Classification> classificationList;
	
	@OneToMany(
			mappedBy = "classificationDataConceptID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXClassification> classifications;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptSecurityToken> securities;
	
	@OneToMany(
			mappedBy = "classificationDataConceptID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXResourceItem> classificationDataConceptXResourceItemList;
	
	public ClassificationDataConcept()
	{
	
	}
	
	public ClassificationDataConcept(java.lang.String classificationDataConceptID)
	{
		id = classificationDataConceptID;
	}
	
	public ClassificationDataConcept(java.lang.String classificationDataConceptID, String classificationDataConceptName, String classificationDataConceptDesc, String originalSourceSystemUniqueID)
	{
		id = classificationDataConceptID;
		name = classificationDataConceptName;
		description = classificationDataConceptDesc;
		
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	
	public void configureForClassification(ClassificationDataConceptXClassification classificationLink, ISystems<?, ?> system)
	{
		classificationLink.setClassificationDataConceptID(this);
	}
	
	public List<Classification> getClassificationList()
	{
		return classificationList;
	}
	
	public ClassificationDataConcept setClassificationList(List<Classification> classificationList)
	{
		this.classificationList = classificationList;
		return this;
	}
	
	public List<ClassificationDataConceptXClassification> getClassifications()
	{
		return classifications;
	}
	
	public ClassificationDataConcept setClassifications(List<ClassificationDataConceptXClassification> classifications)
	{
		this.classifications = classifications;
		return this;
	}
	
	public List<ClassificationDataConceptSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public ClassificationDataConcept setSecurities(List<ClassificationDataConceptSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public List<ClassificationDataConceptXResourceItem> getClassificationDataConceptXResourceItemList()
	{
		return classificationDataConceptXResourceItemList;
	}
	
	public ClassificationDataConcept setClassificationDataConceptXResourceItemList(List<ClassificationDataConceptXResourceItem> classificationDataConceptXResourceItemList)
	{
		this.classificationDataConceptXResourceItemList = classificationDataConceptXResourceItemList;
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
		ClassificationDataConcept that = (ClassificationDataConcept) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public java.lang.String getId()
	{
		return id;
	}
	
	@Override
	public ClassificationDataConcept setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public String getName()
	{
		return name;
	}
	
	public ClassificationDataConcept setName(String name)
	{
		this.name = name;
		return this;
	}
	
	public @NotNull @Size(min = 1,
	                      max = 1500) String getDescription()
	{
		return description;
	}
	
	public ClassificationDataConcept setDescription(@NotNull @Size(min = 1,
	                                                               max = 1500) String description)
	{
		this.description = description;
		return this;
	}
	
	@Override
	public void configureResourceItemAddable(IWarehouseRelationshipTable linkTable, ClassificationDataConcept primary, IResourceItem<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
	{
		ClassificationDataConceptXResourceItem cdc = (ClassificationDataConceptXResourceItem) linkTable;
		cdc.setClassificationDataConceptID(primary);
		cdc.setResourceItemID((ResourceItem) secondary);
		cdc.setClassificationID(classificationValue);
		cdc.setValue(Strings.nullToEmpty(value));
	}
}
