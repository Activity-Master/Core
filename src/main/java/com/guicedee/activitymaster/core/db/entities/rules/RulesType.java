package com.guicedee.activitymaster.core.db.entities.rules;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.client.services.builders.warehouse.rules.IRulesType;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesTypeQueryBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(schema = "Rules",
       name = "RulesType")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class RulesType
		extends WarehouseSCDNameDescriptionTable<RulesType, RulesTypeQueryBuilder, java.util.UUID>
		implements IRulesType<RulesType, RulesTypeQueryBuilder>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesTypeID")
	@JsonValue
	@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
	      max = 200)
	@Column(nullable = false,
	        length = 200,
	        name = "RulesTypeName")
	private String name;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
	      max = 200)
	@Column(nullable = false,
	        length = 200,
	        name = "RulesTypeDesc")
	private String description;
	
	@OneToMany(
			mappedBy = "rulesTypeID",
			fetch = FetchType.LAZY)
	private List<RulesXRulesType> rulesXRulesTypeList;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<RulesTypeSecurityToken> securities;
	@OneToMany(
			mappedBy = "rulesTypeID",
			fetch = FetchType.LAZY)
	private List<RulesTypeXResourceItem> resources;
	
	@OneToMany(
			mappedBy = "rulesTypeID",
			fetch = FetchType.LAZY)
	private List<RulesTypeXClassification> classifications;
	
	
	public RulesType()
	{
	
	}
	
	public RulesType(UUID rulesTypeID)
	{
		this.id = rulesTypeID;
	}
	
	public RulesType(UUID rulesTypeID, String rulesTypName, String rulesTypeDesc)
	{
		this.id = rulesTypeID;
		this.name = rulesTypName;
		this.description = rulesTypeDesc;
	}
	
	public List<RulesXRulesType> getRulesXRulesTypeList()
	{
		return this.rulesXRulesTypeList;
	}
	
	public List<RulesTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public RulesType setRulesXRulesTypeList(List<RulesXRulesType> rulesXRulesTypeList)
	{
		this.rulesXRulesTypeList = rulesXRulesTypeList;
		return this;
	}
	
	public List<RulesTypeXClassification> getClassifications()
	{
		return classifications;
	}
	
	public void setClassifications(List<RulesTypeXClassification> classifications)
	{
		this.classifications = classifications;
	}
	
	public RulesType setSecurities(List<RulesTypeSecurityToken> securities)
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
		RulesType rulesType = (RulesType) o;
		return Objects.equals(getName(), rulesType.getName());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	@Override
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	@Override
	public @NotNull @Size(min = 1,
	                      max = 200) String getName()
	{
		return this.name;
	}
	
	@Override
	public @NotNull @Size(min = 1,
	                      max = 200) String getDescription()
	{
		return this.description;
	}
	
	@Override
	public RulesType setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public RulesType setName(@NotNull @Size(min = 1,
	                                        max = 200) String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	public RulesType setDescription(@NotNull @Size(min = 1,
	                                               max = 200) String description)
	{
		this.description = description;
		return this;
	}
	
	
	@Override
	public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable, IClassification<?, ?> classificationValue, ISystems<?, ?> system)
	{
		RulesTypeXClassification r = (RulesTypeXClassification) linkTable;
		r.setRulesTypeID(this);
	}
	
	@Override
	public void configureResourceItemAddable(IWarehouseRelationshipTable linkTable, RulesType primary, IResourceItem<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
	{
		RulesTypeXResourceItem r = (RulesTypeXResourceItem) linkTable;
		r.setRulesTypeID(primary);
		r.setResourceItemID((ResourceItem) secondary);
		r.setClassificationID(classificationValue);
		r.setValue(value);
	}
	
}
