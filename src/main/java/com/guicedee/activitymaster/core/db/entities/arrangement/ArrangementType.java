package com.guicedee.activitymaster.core.db.entities.arrangement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementTypeQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.classifications.arrangement.IArrangementClassification;
import com.guicedee.activitymaster.core.services.dto.IArrangementType;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import com.guicedee.activitymaster.core.services.enumtypes.IArrangementTypes;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(schema = "Arrangement",
       name = "ArrangementType")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
public class ArrangementType
		extends WarehouseSCDNameDescriptionTable<ArrangementType, ArrangementTypeQueryBuilder, java.util.UUID, ArrangementTypeSecurityToken>
		implements IContainsClassifications<ArrangementType, Classification, ArrangementTypeXClassification, IArrangementClassification<?>, IArrangementType<?>, IClassification<?>, ArrangementType>,
		           IArrangementType<ArrangementType>,
		           IArrangementTypes
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
	        name = "ArrangementTypeID")
	@JsonValue@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	@Basic(optional = false,
	       fetch = FetchType.EAGER)
	@NotNull
	@Size(min = 1,
	      max = 150)
	@Column(nullable = false,
	        length = 150,
	        name = "ArrangementTypeName")
	private String name;
	@Basic(optional = false,
	       fetch = FetchType.EAGER)
	@NotNull
	@Size(min = 1,
	      max = 500)
	@Column(nullable = false,
	        length = 500,
	        name = "ArrangementTypeDescription")
	private String description;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ArrangementTypeSecurityToken> securities;
	
	@OneToMany(
			mappedBy = "arrangementID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ArrangementTypeXClassification> classifications;
	
	@OneToMany(
			mappedBy = "type",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ArrangementXArrangementType> arrangementsList;
	
	
	public ArrangementType()
	{
	
	}
	
	public ArrangementType(UUID arrangementTypeID)
	{
		this.id = arrangementTypeID;
	}
	
	public ArrangementType(UUID arrangementTypeID, String arrangementTypeName, String arrangementTypeDescription)
	{
		this.id = arrangementTypeID;
		this.name = arrangementTypeName;
		this.description = arrangementTypeDescription;
	}
	
	@Override
	protected ArrangementTypeSecurityToken configureDefaultsForNewToken(ArrangementTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	public List<ArrangementTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public List<ArrangementXArrangementType> getArrangementsList()
	{
		return this.arrangementsList;
	}
	
	public ArrangementType setSecurities(List<ArrangementTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public ArrangementType setArrangementsList(List<ArrangementXArrangementType> arrangementsList)
	{
		this.arrangementsList = arrangementsList;
		return this;
	}
	
	@Override
	public String toString()
	{
		return "ArrangementType - " + getName();
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
		ArrangementType that = (ArrangementType) o;
		return Objects.equals(getName(), that.getName());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	@Override
	public ArrangementType setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public ArrangementType setName(String name)
	{
		this.name = name;
		return this;
	}
	
	public ArrangementType setDescription(@NotNull @Size(min = 1,
	                                                     max = 500) String description)
	{
		this.description = description;
		return this;
	}
	
	@Override
	public void configureForClassification(ArrangementTypeXClassification classificationLink, IEnterprise<?> enterprise)
	{
		classificationLink.setArrangementTypeID(this);
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
	public String classificationDescription()
	{
		return getDescription();
	}
	
	@Override
	public IClassificationDataConceptValue<?> concept()
	{
		return null;
	}
}
