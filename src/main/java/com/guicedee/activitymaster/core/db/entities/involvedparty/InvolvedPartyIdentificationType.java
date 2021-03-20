package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.client.services.builders.warehouse.party.IInvolvedPartyIdentificationType;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyIdentificationTypeQueryBuilder;
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
@SuppressWarnings("unused")
@Entity
@Table(schema = "Party",
       name = "InvolvedPartyIdentificationType")
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
public class InvolvedPartyIdentificationType
		extends WarehouseSCDNameDescriptionTable<InvolvedPartyIdentificationType, InvolvedPartyIdentificationTypeQueryBuilder, java.util.UUID>
		implements IInvolvedPartyIdentificationType<InvolvedPartyIdentificationType, InvolvedPartyIdentificationTypeQueryBuilder>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyIdentificationTypeID")
	@JsonValue
	@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 150)
	@Column(nullable = false,
	        length = 150,
	        name = "InvolvedPartyIdentificationName")
	private String name;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 500)
	@Column(nullable = false,
	        length = 500,
	        name = "InvolvedPartyIdentificationDesc")
	private String description;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyIdentificationTypeSecurityToken> securities;
	
	@OneToMany(
			mappedBy = "involvedPartyIdentificationTypeID",
			fetch = FetchType.LAZY)
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
	public String toString()
	{
		return getName();
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
	
}
