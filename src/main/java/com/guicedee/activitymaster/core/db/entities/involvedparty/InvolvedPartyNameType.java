package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.client.services.builders.warehouse.party.IInvolvedPartyNameType;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyNameTypeQueryBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;
import static jakarta.persistence.FetchType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "InvolvedPartyNameType",
       schema = "Party")
@XmlRootElement
@Access(FIELD)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class InvolvedPartyNameType
		extends WarehouseSCDNameDescriptionTable<InvolvedPartyNameType, InvolvedPartyNameTypeQueryBuilder, java.util.UUID>
		implements IInvolvedPartyNameType<InvolvedPartyNameType,InvolvedPartyNameTypeQueryBuilder>

{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
	        name = "InvolvedPartyNameTypeID")
	@JsonValue@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 500)
	@Column(nullable = false,
	        length = 500,
	        name = "InvolvedPartyNameTypeName")
		private String name;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 500)
	@Column(nullable = false,
	        length = 500,
	        name = "InvolvedPartyNameTypeDescr")
		private String description;
	
	@OneToMany(
			mappedBy = "involvedPartyNameTypeID",
			fetch = FetchType.LAZY)
		private List<InvolvedPartyXInvolvedPartyNameType> involvedPartyXInvolvedPartyNameTypeList;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
		private List<InvolvedPartyNameTypeSecurityToken> securities;
	
	public InvolvedPartyNameType()
	{
	
	}
	
	public InvolvedPartyNameType(UUID involvedPartyNameTypeID)
	{
		id = involvedPartyNameTypeID;
	}
	
	public InvolvedPartyNameType(UUID involvedPartyNameTypeID, String involvedPartyName, String involvedPartyNameDescr)
	{
		id = involvedPartyNameTypeID;
		name = involvedPartyName;
		description = involvedPartyNameDescr;
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	public List<InvolvedPartyXInvolvedPartyNameType> getInvolvedPartyXInvolvedPartyNameTypeList()
	{
		return involvedPartyXInvolvedPartyNameTypeList;
	}
	
	public List<InvolvedPartyNameTypeSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public InvolvedPartyNameType setInvolvedPartyXInvolvedPartyNameTypeList(List<InvolvedPartyXInvolvedPartyNameType> involvedPartyXInvolvedPartyNameTypeList)
	{
		this.involvedPartyXInvolvedPartyNameTypeList = involvedPartyXInvolvedPartyNameTypeList;
		return this;
	}
	
	public InvolvedPartyNameType setSecurities(List<InvolvedPartyNameTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof InvolvedPartyNameType))
		{
			return false;
		}
		InvolvedPartyNameType other = (InvolvedPartyNameType) o;
		if (!other.canEqual((Object) this))
		{
			return false;
		}
		Object this$id = getId();
		Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
		{
			return false;
		}
		return true;
	}
	
	protected boolean canEqual(Object other)
	{
		return other instanceof InvolvedPartyNameType;
	}
	
	@Override
	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		Object $id = getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
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
	public InvolvedPartyNameType setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public InvolvedPartyNameType setName(@NotNull @Size(min = 1,
	                                                    max = 500) String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	public InvolvedPartyNameType setDescription(@NotNull @Size(min = 1,
	                                                           max = 500) String description)
	{
		this.description = description;
		return this;
	}
}
