package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsActiveFlags;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.IContainsNameAndDescription;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IInvolvedPartyIdentificationType;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IIdentificationType;
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
@Table(schema = "Party",
       name = "InvolvedPartyIdentificationType")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
public class InvolvedPartyIdentificationType
		extends WarehouseSCDNameDescriptionTable<InvolvedPartyIdentificationType, InvolvedPartyIdentificationTypeQueryBuilder, java.util.UUID, InvolvedPartyIdentificationTypeSecurityToken>
		implements IInvolvedPartyIdentificationType<InvolvedPartyIdentificationType>,
		           IContainsNameAndDescription<InvolvedPartyIdentificationType>,
		           IContainsEnterprise<InvolvedPartyIdentificationType>,
		           IActivityMasterEntity<InvolvedPartyIdentificationType>,
		           IContainsActiveFlags<InvolvedPartyIdentificationType>,
		           IIdentificationType
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyIdentificationTypeID")
	@JsonValue@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 150)
	@Column(nullable = false,
	        length = 150,
	        name = "InvolvedPartyIdentificationName")
	@JsonIgnore
	private String name;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 500)
	@Column(nullable = false,
	        length = 500,
	        name = "InvolvedPartyIdentificationDesc")
	@JsonIgnore
	private String description;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<InvolvedPartyIdentificationTypeSecurityToken> securities;
	
	@OneToMany(
			mappedBy = "involvedPartyIdentificationTypeID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<InvolvedPartyXInvolvedPartyIdentificationType> involvedPartyXInvolvedPartyIdentificationTypeList;
	
	public InvolvedPartyIdentificationType()
	{
	
	}
	
	public InvolvedPartyIdentificationType(UUID involvedPartyIdentificationTypeID)
	{
		id = involvedPartyIdentificationTypeID;
	}
	
	public InvolvedPartyIdentificationType(UUID involvedPartyIdentificationTypeID, String involvedPartyIdentificationName, String involvedPartyIdentificationDesc)
	{
		id = involvedPartyIdentificationTypeID;
		name = involvedPartyIdentificationName;
		description = involvedPartyIdentificationDesc;
	}
	
	@Override
	protected InvolvedPartyIdentificationTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyIdentificationTypeSecurityToken stAdmin,  ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	@Override
	public String toString()
	{
		return "IdentificationType - " + getName();
	}
	
	public List<InvolvedPartyIdentificationTypeSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public List<InvolvedPartyXInvolvedPartyIdentificationType> getInvolvedPartyXInvolvedPartyIdentificationTypeList()
	{
		return involvedPartyXInvolvedPartyIdentificationTypeList;
	}
	
	public InvolvedPartyIdentificationType setSecurities(List<InvolvedPartyIdentificationTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public InvolvedPartyIdentificationType setInvolvedPartyXInvolvedPartyIdentificationTypeList(List<InvolvedPartyXInvolvedPartyIdentificationType> involvedPartyXInvolvedPartyIdentificationTypeList)
	{
		this.involvedPartyXInvolvedPartyIdentificationTypeList = involvedPartyXInvolvedPartyIdentificationTypeList;
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
		InvolvedPartyIdentificationType that = (InvolvedPartyIdentificationType) o;
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
		return id;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getDescription()
	{
		return description;
	}
	
	@Override
	public InvolvedPartyIdentificationType setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public InvolvedPartyIdentificationType setName(String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	public InvolvedPartyIdentificationType setDescription(@NotNull @Size(min = 1,
	                                                                     max = 500) String description)
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
	
	@Override
	public String classificationDescription()
	{
		return description;
	}
}
