package com.guicedee.activitymaster.core.db.entities.rules;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.product.ProductTypeXClassification;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesTypeQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.capabilities.IContainsResourceItems;
import com.guicedee.activitymaster.core.services.classifications.rules.IRulesTypeClassification;
import com.guicedee.activitymaster.core.services.dto.*;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.enumtypes.IRulesTypeValue;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
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
@SuppressWarnings("unused")
@Entity
@Table(schema = "Rules",
       name = "RulesType")
@XmlRootElement

@Access(FIELD)@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class RulesType
		extends WarehouseSCDNameDescriptionTable<RulesType, RulesTypeQueryBuilder, java.util.UUID, RulesTypeSecurityToken>
		implements IRulesType<RulesType>,
		           IActivityMasterEntity<RulesType>,
		           IContainsClassifications<RulesType, Classification,RulesTypeXClassification, IRulesTypeClassification<?>,IRulesType<?>, IClassification<?>,RulesType>,
		           IRulesTypeValue,
		           IContainsResourceItems<RulesType, ResourceItem, RulesTypeXResourceItem, IClassificationValue<?>,IRulesType<?>, IResourceItem<?>,RulesType>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesTypeID")
	@JsonValue@org.hibernate.annotations.Type(type = "uuid-char")
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
	
	@Override
	protected RulesTypeSecurityToken configureDefaultsForNewToken(RulesTypeSecurityToken stAdmin,  ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
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
		return "RulesType - " + getName();
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
	public void configureForClassification(RulesTypeXClassification classificationLink, ISystems<?> system)
	{
		classificationLink.setRulesTypeID(this);
	}
	
	@Override
	public String name()
	{
		return getName();
	}
	
	@Override
	public String classificationValue()
	{
		return getName();
	}
	
	@Override
	public void configureResourceItemLinkValue(RulesTypeXResourceItem linkTable, RulesType primary, ResourceItem secondary, IClassification<?> classificationValue, String value, ISystems<?> system)
	{
		linkTable.setRulesTypeID(primary);
		linkTable.setResourceItemID(secondary);
		linkTable.setClassificationID((Classification) classificationValue);
		linkTable.setValue(value);
		linkTable.setSystemID((Systems) system);
	}
	
	@Override
	public void configureResourceItemAddable(RulesTypeXResourceItem linkTable, RulesType primary, ResourceItem secondary, IClassificationValue<?> classificationValue, String value, ISystems<?> system)
	{
		linkTable.setRulesTypeID(primary);
		linkTable.setResourceItemID(secondary);
		linkTable.setClassificationID((Classification) classificationValue);
		linkTable.setValue(value);
		linkTable.setSystemID((Systems) system);
	}
}
