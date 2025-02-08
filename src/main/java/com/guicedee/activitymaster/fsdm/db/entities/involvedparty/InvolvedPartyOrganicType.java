package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseNameAndDescriptionTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyOrganicTypeQueryBuilder;
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
       name = "InvolvedPartyOrganicType")
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
public class InvolvedPartyOrganicType
		extends WarehouseSCDTable<InvolvedPartyOrganicType, InvolvedPartyOrganicTypeQueryBuilder, String, InvolvedPartyOrganicTypeSecurityToken>
		implements IWarehouseNameAndDescriptionTable<InvolvedPartyOrganicType,InvolvedPartyOrganicTypeQueryBuilder,String>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyOrganicTypeID")
	@JsonValue
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 200)
	@Column(nullable = false,
	        length = 200,
	        name = "InvolvedPartyTypeName")
	private String name;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 500)
	@Column(nullable = false,
	        length = 500,
	        name = "InvolvedPartyTypeDesc")
	private String description;
	
@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<InvolvedPartyOrganicTypeSecurityToken> securities;
	
	public InvolvedPartyOrganicType()
	{
	
	}
	
	public InvolvedPartyOrganicType(java.lang.String involvedPartyOrganicTypeID)
	{
		id = involvedPartyOrganicTypeID;
	}
	
	@Override
	public void configureSecurityEntity(InvolvedPartyOrganicTypeSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	@Override
	public String toString()
	{
		return "OrganicPartyType - " + getName();
	}
	
	public List<InvolvedPartyOrganicTypeSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public InvolvedPartyOrganicType setSecurities(List<InvolvedPartyOrganicTypeSecurityToken> securities)
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
		InvolvedPartyOrganicType that = (InvolvedPartyOrganicType) o;
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
	public InvolvedPartyOrganicType setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public @NotNull @Size(min = 1,
	                      max = 200) String getName()
	{
		return name;
	}
	
	@Override
	public InvolvedPartyOrganicType setName(@NotNull @Size(min = 1,
	                                                       max = 200) String name)
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
	public InvolvedPartyOrganicType setDescription(@NotNull @Size(min = 1,
	                                                              max = 500) String description)
	{
		this.description = description;
		return this;
	}
}
