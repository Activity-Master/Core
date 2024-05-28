package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseNameAndDescriptionTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedPartyIdentificationType;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyIdentificationTypeQueryBuilder;
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
@SuppressWarnings("unused")
@Entity
@Table(schema = "Party",
       name = "InvolvedPartyIdentificationType")
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
public class InvolvedPartyIdentificationType
		extends WarehouseSCDTable<InvolvedPartyIdentificationType, InvolvedPartyIdentificationTypeQueryBuilder, String,
				InvolvedPartyIdentificationTypeSecurityToken>
		implements IInvolvedPartyIdentificationType<InvolvedPartyIdentificationType, InvolvedPartyIdentificationTypeQueryBuilder>,
		           IWarehouseNameAndDescriptionTable<InvolvedPartyIdentificationType,InvolvedPartyIdentificationTypeQueryBuilder,String>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyIdentificationTypeID")
	@JsonValue
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
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
	
	public InvolvedPartyIdentificationType(java.lang.String involvedPartyIdentificationTypeID)
	{
		id = involvedPartyIdentificationTypeID;
	}
	
	public InvolvedPartyIdentificationType(java.lang.String involvedPartyIdentificationTypeID, String involvedPartyIdentificationName, String involvedPartyIdentificationDesc)
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
	
	public InvolvedPartyIdentificationType setSecurities(List<InvolvedPartyIdentificationTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public List<InvolvedPartyXInvolvedPartyIdentificationType> getInvolvedPartyXInvolvedPartyIdentificationTypeList()
	{
		return involvedPartyXInvolvedPartyIdentificationTypeList;
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
	public java.lang.String getId()
	{
		return id;
	}
	
	@Override
	public InvolvedPartyIdentificationType setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public InvolvedPartyIdentificationType setName(String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	public String getDescription()
	{
		return description;
	}
	
	@Override
	public InvolvedPartyIdentificationType setDescription(@NotNull @Size(min = 1,
	                                                                     max = 500) String description)
	{
		this.description = description;
		return this;
	}
	
}
