package com.guicedee.activitymaster.core.db.entities.classifications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.classifications.builders.ClassificationDataConceptQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsResourceItems;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;
import static jakarta.persistence.FetchType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(schema = "Classification",
       name = "ClassificationDataConcept")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
public class ClassificationDataConcept
		extends WarehouseSCDNameDescriptionTable<ClassificationDataConcept, ClassificationDataConceptQueryBuilder, java.util.UUID, ClassificationDataConceptSecurityToken>
		implements// IContainsClassifications<ClassificationDataConcept, Classification, ClassificationDataConceptXClassification, IClassificationClassificationDataConceptTypes<?>>,
		IContainsResourceItems<ClassificationDataConcept, ResourceItem, ClassificationDataConceptXResourceItem, IClassificationValue<?>, IClassificationDataConcept<?>, IResourceItem<?>, ClassificationDataConcept>,
		IActivityMasterEntity<ClassificationDataConcept>,
		IClassificationDataConcept<ClassificationDataConcept>,
		IClassificationDataConceptValue
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
	        name = "ClassificationDataConceptID")
	@JsonValue@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 100)
	@Column(nullable = false,
	        length = 100,
	        name = "classificationDataConceptName")
	@JsonIgnore
	private String name;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 1500)
	@Column(nullable = false,
	        length = 1500,
	        name = "ClassificationDataConceptDesc")
	@JsonIgnore
	private String description;
	
	@OneToMany(
			mappedBy = "concept",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Classification> classificationList;
	
	@OneToMany(
			mappedBy = "classificationDataConceptID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ClassificationDataConceptXClassification> classifications;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ClassificationDataConceptSecurityToken> securities;
	
	@OneToMany(
			mappedBy = "classificationDataConceptID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ClassificationDataConceptXResourceItem> classificationDataConceptXResourceItemList;
	
	public ClassificationDataConcept()
	{
	
	}
	
	public ClassificationDataConcept(UUID classificationDataConceptID)
	{
		id = classificationDataConceptID;
	}
	
	public ClassificationDataConcept(UUID classificationDataConceptID, String classificationDataConceptName, String classificationDataConceptDesc, String originalSourceSystemUniqueID)
	{
		id = classificationDataConceptID;
		name = classificationDataConceptName;
		description = classificationDataConceptDesc;
		
	}
	
	@Override
	public String toString()
	{
		return "ClassificationDataConcept - " + getName();
	}
	
	@Override
	protected ClassificationDataConceptSecurityToken configureDefaultsForNewToken(ClassificationDataConceptSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		ClassificationDataConceptSecurityToken token = super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem);
		token.setBase(this);
		return token;
	}
	
	public void configureForClassification(ClassificationDataConceptXClassification classificationLink, IEnterprise<?> enterprise)
	{
		classificationLink.setClassificationDataConceptID(this);
	}
	
	@Override
	public void configureResourceItemLinkValue(ClassificationDataConceptXResourceItem linkTable, ClassificationDataConcept primary, ResourceItem secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setClassificationDataConceptID(this);
		linkTable.setResourceItemID(secondary);
	}
	
	@Override
	public void configureResourceItemAddable(ClassificationDataConceptXResourceItem linkTable, ClassificationDataConcept primary, ResourceItem secondary, IClassificationValue<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setClassificationDataConceptID(this);
		linkTable.setResourceItemID(secondary);
	}
	
	public List<Classification> getClassificationList()
	{
		return classificationList;
	}
	
	public List<ClassificationDataConceptXClassification> getClassifications()
	{
		return classifications;
	}
	
	public List<ClassificationDataConceptSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public List<ClassificationDataConceptXResourceItem> getClassificationDataConceptXResourceItemList()
	{
		return classificationDataConceptXResourceItemList;
	}
	
	public ClassificationDataConcept setClassificationList(List<Classification> classificationList)
	{
		this.classificationList = classificationList;
		return this;
	}
	
	public ClassificationDataConcept setClassifications(List<ClassificationDataConceptXClassification> classifications)
	{
		this.classifications = classifications;
		return this;
	}
	
	public ClassificationDataConcept setSecurities(List<ClassificationDataConceptSecurityToken> securities)
	{
		this.securities = securities;
		return this;
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
	public java.util.UUID getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public @NotNull @Size(min = 1,
	                      max = 1500) String getDescription()
	{
		return description;
	}
	
	@Override
	public ClassificationDataConcept setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public ClassificationDataConcept setName(String name)
	{
		this.name = name;
		return this;
	}
	
	public ClassificationDataConcept setDescription(@NotNull @Size(min = 1,
	                                                               max = 1500) String description)
	{
		this.description = description;
		return this;
	}
	
	@Override
	public String name()
	{
		return name;
	}
	
	@Override
	public String classificationValue()
	{
		return name;
	}
}
