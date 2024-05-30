/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyQueryBuilder;
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
@Table(schema = "Party", name = "InvolvedPartyXInvolvedParty")
@XmlRootElement

@Access(AccessType.FIELD)
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
		java.lang.String,
		InvolvedPartyXInvolvedPartySecurityToken>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyXInvolvedPartyID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
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
	
	public InvolvedPartyXInvolvedParty(java.lang.String involvedPartyXInvolvedPartyID)
	{
		this.id = involvedPartyXInvolvedPartyID;
	}
	
	@Override
	public void configureSecurityEntity(InvolvedPartyXInvolvedPartySecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public InvolvedPartyXInvolvedParty setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedParty getChildInvolvedPartyID()
	{
		return this.childInvolvedPartyID;
	}
	
	public InvolvedPartyXInvolvedParty setChildInvolvedPartyID(InvolvedParty childInvolvedPartyID)
	{
		this.childInvolvedPartyID = childInvolvedPartyID;
		return this;
	}
	
	public InvolvedParty getParentInvolvedPartyID()
	{
		return this.parentInvolvedPartyID;
	}
	
	public InvolvedPartyXInvolvedParty setParentInvolvedPartyID(InvolvedParty parentInvolvedPartyID)
	{
		this.parentInvolvedPartyID = parentInvolvedPartyID;
		return this;
	}
	
	public List<InvolvedPartyXInvolvedPartySecurityToken> getSecurities()
	{
		return this.securities;
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
