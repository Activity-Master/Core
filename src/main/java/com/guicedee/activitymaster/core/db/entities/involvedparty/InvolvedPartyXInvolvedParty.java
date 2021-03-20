/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyQueryBuilder;
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
@Table(schema = "Party", name = "InvolvedPartyXInvolvedParty")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class InvolvedPartyXInvolvedParty
		extends WarehouseClassificationRelationshipTable<InvolvedParty,
		InvolvedParty,
		InvolvedPartyXInvolvedParty,
		InvolvedPartyXInvolvedPartyQueryBuilder,
		java.util.UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyXInvolvedPartyID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
	@JoinColumn(name = "ChildInvolvedPartyID",
	            referencedColumnName = "InvolvedPartyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private InvolvedParty childInvolvedPartyID;
	@JoinColumn(name = "ParentInvolvedPartyID",
	            referencedColumnName = "InvolvedPartyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private InvolvedParty parentInvolvedPartyID;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartySecurityToken> securities;
	
	public InvolvedPartyXInvolvedParty()
	{
	
	}
	
	public InvolvedPartyXInvolvedParty(UUID involvedPartyXInvolvedPartyID)
	{
		this.id = involvedPartyXInvolvedPartyID;
	}
	
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	public InvolvedParty getChildInvolvedPartyID()
	{
		return this.childInvolvedPartyID;
	}
	
	public InvolvedParty getParentInvolvedPartyID()
	{
		return this.parentInvolvedPartyID;
	}
	
	public List<InvolvedPartyXInvolvedPartySecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public InvolvedPartyXInvolvedParty setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedPartyXInvolvedParty setChildInvolvedPartyID(InvolvedParty childInvolvedPartyID)
	{
		this.childInvolvedPartyID = childInvolvedPartyID;
		return this;
	}
	
	public InvolvedPartyXInvolvedParty setParentInvolvedPartyID(InvolvedParty parentInvolvedPartyID)
	{
		this.parentInvolvedPartyID = parentInvolvedPartyID;
		return this;
	}
	
	public InvolvedPartyXInvolvedParty setSecurities(List<InvolvedPartyXInvolvedPartySecurityToken> securities)
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
		InvolvedPartyXInvolvedParty that = (InvolvedPartyXInvolvedParty) o;
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
		return getParentInvolvedPartyID();
	}
	
	@Override
	public InvolvedParty getSecondary()
	{
		return getChildInvolvedPartyID();
	}
}
