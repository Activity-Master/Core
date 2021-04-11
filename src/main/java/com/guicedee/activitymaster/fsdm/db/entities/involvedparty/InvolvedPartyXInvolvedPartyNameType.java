/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyNameTypeQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.*;

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
		UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
	        name = "InvolvedPartyXInvolvedPartyNameTypeID")@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
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
	public UUID getId()
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
	public InvolvedPartyXInvolvedPartyNameType setId(UUID id)
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
	public InvolvedParty getPrimary()
	{
		return getInvolvedPartyID();
	}
	
	@Override
	public InvolvedPartyNameType getSecondary()
	{
		return getInvolvedPartyNameTypeID();
	}
}
