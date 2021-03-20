package com.guicedee.activitymaster.core.db.entities.classifications;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.core.ClassificationService;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.entities.classifications.builders.ClassificationQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;
import static jakarta.persistence.FetchType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings({"unused", "rawtypes"})
@Entity
@Table(schema = "Classification",
       name = "Classification")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class Classification
		extends WarehouseTable<Classification, ClassificationQueryBuilder, java.util.UUID>
		implements IClassification<Classification, ClassificationQueryBuilder>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationID")
	@JsonValue@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 100)
	@Column(nullable = false,
	        length = 100,
	        name = "ClassificationName")
		private String name;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 500)
	@Column(nullable = false,
	        length = 500,
	        name = "ClassificationDesc")
		private String description;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Column(nullable = false,
	        name = "ClassificationSequenceNumber")
		@OrderBy
	private int classificationSequenceNumber;
	@JoinColumn(name = "ClassificationDataConceptID",
	            referencedColumnName = "ClassificationDataConceptID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.EAGER)
		private ClassificationDataConcept concept;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
		private List<ClassificationSecurityToken> securities;
	
	public Classification()
	{
	
	}
	
	public Classification(UUID classificationID)
	{
		id = classificationID;
	}
	
	public Classification(UUID classificationID, String classificationName, String classificationDesc, int classificationSequenceNumber)
	{
		id = classificationID;
		name = classificationName;
		description = classificationDesc;
		this.classificationSequenceNumber = classificationSequenceNumber;
	}
	
	public void configureForClassification(ClassificationXClassification classificationLink, ISystems<?,?> system)
	{
		Classification hierarchyClassification = (Classification) GuiceContext.get(ClassificationService.class)
		                                                                      .getHierarchyType(system);
		Classification incomingClassification = (Classification) classificationLink.getClassificationID();
		
		classificationLink.setChildClassificationID(incomingClassification);
		classificationLink.setParentClassificationID(this);
		classificationLink.setClassificationID(hierarchyClassification);
	}
	
	//@Override
	public void configureNewHierarchyItem(ClassificationXClassification newLink, IClassification<?,?> parent, IClassification<?,?> child, String value)
	{
		newLink.setParentClassificationID(this);
		newLink.setChildClassificationID((Classification) child);
		newLink.setEnterpriseID(getEnterpriseID());
	}
	
	
	@Override
	public void configureNewHierarchyItem(IWarehouseRelationshipClassificationTable<?, ?, Classification, Classification, UUID> newLink, Classification parent, Classification child, String value)
	{
		ClassificationXClassification c = (ClassificationXClassification) newLink;
		c.setParentClassificationID(this);
		c.setChildClassificationID(child);
		newLink.setEnterpriseID(getEnterpriseID());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
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
		Classification that = (Classification) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public String toString()
	{
		return "Classification - " + getName() + " - " + getDescription();
	}
	
	@Override
	public java.util.UUID getId()
	{
		return id;
	}
	
	@Override
	public Classification setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public @NotNull Integer getClassificationSequenceNumber()
	{
		return classificationSequenceNumber;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	public Classification setClassificationSequenceNumber(@NotNull Integer classificationSequenceNumber)
	{
		this.classificationSequenceNumber = classificationSequenceNumber;
		return this;
	}
	
	public ClassificationDataConcept getConcept()
	{
		return concept;
	}
	
	@Override
	public String getDescription()
	{
		return description;
	}
	
	public Classification setConcept(ClassificationDataConcept concept)
	{
		this.concept = concept;
		return this;
	}
	
	//@Override
	public void configureResourceItemLinkValue(ClassificationXResourceItem linkTable, Classification primary, ResourceItem secondary, IClassification<?,?> classificationValue, String value, ISystems<?,?> system)
	{
		linkTable.setClassificationID(this);
		linkTable.setResourceItemID(secondary);
	}
	
	@Override
	public Classification setName(String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	public Classification setDescription(@NotNull @Size(min = 1,
	                                                    max = 500) String description)
	{
		this.description = description;
		return this;
	}
	
	//@Override
	public String name()
	{
		return name;
	}
	
	//@Override
	public String classificationDescription()
	{
		return description;
	}
	
	//@Override
	public EnterpriseClassificationDataConcepts concept()
	{
		return EnterpriseClassificationDataConcepts.valueOf(concept.getName());
	}
	
	
	@Override
	public void configureResourceItemAddable(IWarehouseRelationshipTable linkTable, Classification primary, IResourceItem<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?,?> system)
	{
		ClassificationXResourceItem x = (ClassificationXResourceItem) linkTable;
		x.setClassificationID(primary);
		x.setResourceItemID((ResourceItem) secondary);
		x.setClassificationID(classificationValue);
		x.setValue(value);
		
	}
	
	@Override
	public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable, ISystems<?, ?> system)
	{
		ClassificationXClassification c = (ClassificationXClassification) linkTable;
		((ClassificationXClassification) linkTable).setParentClassificationID(this);
	}
}
