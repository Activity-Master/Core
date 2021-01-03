/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyNameTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IInvolvedParty;
import com.guicedee.activitymaster.core.services.dto.IInvolvedPartyNameType;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.INameType;

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
       name = "InvolvedPartyXInvolvedPartyNameType")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class InvolvedPartyXInvolvedPartyNameType
		extends WarehouseClassificationRelationshipTypesTable<InvolvedParty, InvolvedPartyNameType,
		InvolvedPartyXInvolvedPartyNameType,
		InvolvedPartyXInvolvedPartyNameTypeQueryBuilder,
		INameType<?>,
		java.util.UUID,
		InvolvedPartyXInvolvedPartyNameTypeSecurityToken,
		IInvolvedParty<?>,
		IInvolvedPartyNameType<?>>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
	        name = "InvolvedPartyXInvolvedPartyNameTypeID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
	@JoinColumn(name = "InvolvedPartyID",
	            referencedColumnName = "InvolvedPartyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private InvolvedParty involvedPartyID;
	@JoinColumn(name = "InvolvedPartyNameTypeID",
	            referencedColumnName = "InvolvedPartyNameTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private InvolvedPartyNameType involvedPartyNameTypeID;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> securities;
	
	public InvolvedPartyXInvolvedPartyNameType()
	{
	
	}
	
	public InvolvedPartyXInvolvedPartyNameType(UUID involvedPartyXInvolvedPartyNameTypeID)
	{
		this.id = involvedPartyXInvolvedPartyNameTypeID;
	}
	
	public InvolvedPartyXInvolvedPartyNameType(UUID involvedPartyXInvolvedPartyNameTypeID, String involvedPartyName)
	{
		this.id = involvedPartyXInvolvedPartyNameTypeID;
		setValue(involvedPartyName);
	}
	
	@Override
	protected InvolvedPartyXInvolvedPartyNameTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyXInvolvedPartyNameTypeSecurityToken stAdmin,  ISystems<?> enterprise, ISystems<?> activityMasterSystem)
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
	
	public InvolvedPartyNameType getInvolvedPartyNameTypeID()
	{
		return this.involvedPartyNameTypeID;
	}
	
	public List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	@Override
	public InvolvedPartyXInvolvedPartyNameType setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedPartyXInvolvedPartyNameType setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}
	
	public InvolvedPartyXInvolvedPartyNameType setInvolvedPartyNameTypeID(InvolvedPartyNameType involvedPartyNameTypeID)
	{
		this.involvedPartyNameTypeID = involvedPartyNameTypeID;
		return this;
	}
	
	public InvolvedPartyXInvolvedPartyNameType setSecurities(List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> securities)
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
		InvolvedPartyXInvolvedPartyNameType that = (InvolvedPartyXInvolvedPartyNameType) o;
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
	public IInvolvedPartyNameType<?> getSecondary()
	{
		return getInvolvedPartyNameTypeID();
	}
}
