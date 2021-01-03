package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IInvolvedParty;
import com.guicedee.activitymaster.core.services.dto.IInvolvedPartyType;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;

import jakarta.persistence.*;
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
@Entity
@Table(schema = "Party",
       name = "InvolvedPartyXInvolvedPartyType")
@XmlRootElement

@Access(FIELD)
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
		ITypeValue<?>,
		java.util.UUID,
		InvolvedPartyXInvolvedPartyTypeSecurityToken,
		IInvolvedParty<?>, IInvolvedPartyType<?>>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyXInvolvedPartyTypeID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
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
	
	public InvolvedPartyXInvolvedPartyType(UUID involvedPartyXInvolvedPartyTypeID)
	{
		this.id = involvedPartyXInvolvedPartyTypeID;
	}
	
	@Override
	protected InvolvedPartyXInvolvedPartyTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyXInvolvedPartyTypeSecurityToken stAdmin,  ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	@Override
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}
	
	public InvolvedPartyType getInvolvedPartyTypeID()
	{
		return this.involvedPartyTypeID;
	}
	
	public List<InvolvedPartyXInvolvedPartyTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	@Override
	public InvolvedPartyXInvolvedPartyType setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedPartyXInvolvedPartyType setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}
	
	public InvolvedPartyXInvolvedPartyType setInvolvedPartyTypeID(InvolvedPartyType involvedPartyTypeID)
	{
		this.involvedPartyTypeID = involvedPartyTypeID;
		return this;
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
	public IInvolvedParty<?> getPrimary()
	{
		return getInvolvedPartyID();
	}
	
	@Override
	public IInvolvedPartyType<?> getSecondary()
	{
		return getInvolvedPartyTypeID();
	}
}
