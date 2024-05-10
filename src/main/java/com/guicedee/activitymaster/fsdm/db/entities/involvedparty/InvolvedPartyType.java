package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedPartyType;
import com.guicedee.activitymaster.fsdm.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyTypeQueryBuilder;
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
@Entity
@Table(name = "InvolvedPartyType",
       schema = "Party")
@XmlRootElement
@Access(AccessType.FIELD)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class InvolvedPartyType
		extends WarehouseSCDNameDescriptionTable<InvolvedPartyType, InvolvedPartyTypeQueryBuilder, java.lang.String,InvolvedPartyTypeSecurityToken>
		implements IInvolvedPartyType<InvolvedPartyType, InvolvedPartyTypeQueryBuilder>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyTypeID")
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
	        name = "InvolvedPartyTypeName")
	private String name;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Column(nullable = false,
	        name = "InvolvedPartyTypeDesc")
	private String description;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyTypeSecurityToken> securities;
	
	@OneToMany(
			mappedBy = "involvedPartyTypeID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyType> involvedPartyXInvolvedPartyTypeList;
	
	public InvolvedPartyType()
	{
	
	}
	
	public InvolvedPartyType(java.lang.String involvedPartyTypeID)
	{
		id = involvedPartyTypeID;
	}
	
	public InvolvedPartyType(java.lang.String involvedPartyTypeID, String involvedPartyTypeName, String involvedPartyTypeDesc)
	{
		id = involvedPartyTypeID;
		name = involvedPartyTypeName;
		description = involvedPartyTypeDesc;
	}
	
	public List<InvolvedPartyTypeSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public InvolvedPartyType setSecurities(List<InvolvedPartyTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public List<InvolvedPartyXInvolvedPartyType> getInvolvedPartyXInvolvedPartyTypeList()
	{
		return involvedPartyXInvolvedPartyTypeList;
	}
	
	public InvolvedPartyType setInvolvedPartyXInvolvedPartyTypeList(List<InvolvedPartyXInvolvedPartyType> involvedPartyXInvolvedPartyTypeList)
	{
		this.involvedPartyXInvolvedPartyTypeList = involvedPartyXInvolvedPartyTypeList;
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
		InvolvedPartyType that = (InvolvedPartyType) o;
		return Objects.equals(getName(), that.getName());
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
	public java.lang.String getId()
	{
		return id;
	}
	
	@Override
	public InvolvedPartyType setId(java.lang.String id)
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
	public InvolvedPartyType setName(String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	public @NotNull String getDescription()
	{
		return description;
	}
	
	@Override
	public InvolvedPartyType setDescription(@NotNull String description)
	{
		this.description = description;
		return this;
	}
}
