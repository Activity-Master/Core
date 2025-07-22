package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.ClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static com.guicedee.client.IGuiceContext.*;
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
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Classification
		extends WarehouseSCDTable<Classification, ClassificationQueryBuilder, UUID, ClassificationSecurityToken>
		implements IClassification<Classification, ClassificationQueryBuilder>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationID")
	@JsonValue

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
	private int classificationSequenceNumber;
	@JoinColumn(name = "ClassificationDataConceptID",
	            referencedColumnName = "ClassificationDataConceptID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.EAGER)
	private ClassificationDataConcept concept;
	
@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<ClassificationSecurityToken> securities;

	public Classification(UUID classificationID, String classificationName, String classificationDesc, int classificationSequenceNumber)
	{
		id = classificationID;
		name = classificationName;
		description = classificationDesc;
		this.classificationSequenceNumber = classificationSequenceNumber;
	}
	
	@Override
	public void configureSecurityEntity(ClassificationSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	public void configureForClassification(ClassificationXClassification classificationLink, ISystems<?, ?> system)
	{
		Classification hierarchyClassification = (Classification) get(ClassificationService.class)
				.getHierarchyType(system);
		Classification incomingClassification = classificationLink.getClassificationID();
		
		classificationLink.setChildClassificationID(incomingClassification);
		classificationLink.setParentClassificationID(this);
		classificationLink.setClassificationID(hierarchyClassification);
	}
	
	//@Override
	public void configureNewHierarchyItem(ClassificationXClassification newLink, IClassification<?, ?> parent, IClassification<?, ?> child, String value)
	{
		newLink.setParentClassificationID(this);
		newLink.setChildClassificationID((Classification) child);
		newLink.setEnterpriseID(getEnterpriseID());
	}
	
	
	@Override
	public void configureNewHierarchyItem(IWarehouseRelationshipClassificationTable<?, ?, Classification, Classification, UUID,?> newLink, Classification parent, Classification child, String value)
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
		return getName();
	}

	
	public @NotNull Integer getClassificationSequenceNumber()
	{
		return classificationSequenceNumber;
	}
	
	public Classification setClassificationSequenceNumber(@NotNull Integer classificationSequenceNumber)
	{
		this.classificationSequenceNumber = classificationSequenceNumber;
		return this;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public Classification setName(String name)
	{
		this.name = name;
		return this;
	}
	
	public ClassificationDataConcept getConcept()
	{
		return concept;
	}
	
	public Classification setConcept(ClassificationDataConcept concept)
	{
		this.concept = concept;
		return this;
	}
	
	@Override
	public String getDescription()
	{
		return description;
	}
	
	@Override
	public Classification setDescription(@NotNull @Size(min = 1,
	                                                    max = 500) String description)
	{
		this.description = description;
		return this;
	}
	
	//@Override
	public void configureResourceItemLinkValue(ClassificationXResourceItem linkTable, Classification primary, ResourceItem secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
	{
		linkTable.setClassificationID(this);
		linkTable.setResourceItemID(secondary);
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
	public void configureResourceItemAddable(IWarehouseRelationshipTable linkTable, Classification primary, IResourceItem<?, ?> secondary, IClassification<?, ?> classificationValue, String value, IEnterprise<?,?> enterprise)
	{
		ClassificationXResourceItem x = (ClassificationXResourceItem) linkTable;
		x.setClassificationID(primary);
		x.setResourceItemID((ResourceItem) secondary);
		x.setClassificationID(classificationValue);
		x.setValue(value);
		
	}
	
	@Override
	public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable, IClassification<?, ?> classificationValue, ISystems<?, ?> system)
	{
		ClassificationXClassification c = (ClassificationXClassification) linkTable;

		c.setParentClassificationID(this);
		c.setChildClassificationID((Classification) classificationValue);

		IClassificationService<?> classificationService = get(IClassificationService.class);
		c.setClassificationID(classificationService.getNoClassification(system).await().atMost(Duration.of(50L, ChronoUnit.SECONDS)));
	}
}
