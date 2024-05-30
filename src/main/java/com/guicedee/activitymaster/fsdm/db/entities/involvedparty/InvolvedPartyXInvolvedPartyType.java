package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyTypeQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Party",
       name = "InvolvedPartyXInvolvedPartyType")
@XmlRootElement

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class InvolvedPartyXInvolvedPartyType
		extends WarehouseClassificationRelationshipTypesTable<InvolvedParty,
		InvolvedPartyType,
		InvolvedPartyXInvolvedPartyType,
		InvolvedPartyXInvolvedPartyTypeQueryBuilder,
		java.lang.String,
		InvolvedPartyXInvolvedPartyTypeSecurityToken>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyXInvolvedPartyTypeID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "InvolvedPartyID",
	            referencedColumnName = "InvolvedPartyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private InvolvedParty involvedPartyID;
	@JoinColumn(name = "InvolvedPartyTypeID",
	            referencedColumnName = "InvolvedPartyTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private InvolvedPartyType involvedPartyTypeID;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyTypeSecurityToken> securities;
	
	public InvolvedPartyXInvolvedPartyType()
	{
	
	}
	
	public InvolvedPartyXInvolvedPartyType(java.lang.String involvedPartyXInvolvedPartyTypeID)
	{
		this.id = involvedPartyXInvolvedPartyTypeID;
	}
	
	@Override
	public void configureSecurityEntity(InvolvedPartyXInvolvedPartyTypeSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	@Override
	public java.lang.String getId()
	{
		return this.id;
	}
	
	@Override
	public InvolvedPartyXInvolvedPartyType setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}
	
	public InvolvedPartyXInvolvedPartyType setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}
	
	public InvolvedPartyType getInvolvedPartyTypeID()
	{
		return this.involvedPartyTypeID;
	}
	
	public InvolvedPartyXInvolvedPartyType setInvolvedPartyTypeID(InvolvedPartyType involvedPartyTypeID)
	{
		this.involvedPartyTypeID = involvedPartyTypeID;
		return this;
	}
	
	public List<InvolvedPartyXInvolvedPartyTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public InvolvedPartyXInvolvedPartyType setSecurities(List<InvolvedPartyXInvolvedPartyTypeSecurityToken> securities)
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
		InvolvedPartyXInvolvedPartyType that = (InvolvedPartyXInvolvedPartyType) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public InvolvedParty getPrimary()
	{
		return getInvolvedPartyID();
	}
	
	@Override
	public InvolvedPartyType getSecondary()
	{
		return getInvolvedPartyTypeID();
	}
}
