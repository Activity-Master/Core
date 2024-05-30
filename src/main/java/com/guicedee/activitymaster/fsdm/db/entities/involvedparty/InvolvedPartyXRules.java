/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXRulesQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.rules.Rules;
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
@Table(schema = "Party", name = "InvolvedPartyXRules")
@XmlRootElement

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class InvolvedPartyXRules
		extends WarehouseClassificationRelationshipTable<InvolvedParty,
		Rules,
		InvolvedPartyXRules,
		InvolvedPartyXRulesQueryBuilder,
		java.lang.String,
		InvolvedPartyXRulesSecurityToken>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyXRulesID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXRulesSecurityToken> securities;
	
	@JoinColumn(name = "InvolvedPartyID",
	            referencedColumnName = "InvolvedPartyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private InvolvedParty involvedPartyID;
	
	@OneToOne(mappedBy = "involvedPartyXRules1",
	          fetch = FetchType.LAZY)
	private InvolvedPartyXRules involvedPartyXRules;
	@JoinColumn(name = "InvolvedPartyXRulesID",
	            referencedColumnName = "InvolvedPartyXRulesID",
	            nullable = false,
	            insertable = false,
	            updatable = false)
	@OneToOne(optional = false,
	          fetch = FetchType.LAZY)
	private InvolvedPartyXRules involvedPartyXRules1;
	@JoinColumn(name = "RulesID",
	            referencedColumnName = "RulesID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Rules rulesID;
	
	public InvolvedPartyXRules()
	{
	
	}
	
	public InvolvedPartyXRules(java.lang.String involvedPartyXRulesID)
	{
		this.id = involvedPartyXRulesID;
	}
	
	@Override
	public void configureSecurityEntity(InvolvedPartyXRulesSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public InvolvedPartyXRules setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public List<InvolvedPartyXRulesSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public InvolvedPartyXRules setSecurities(List<InvolvedPartyXRulesSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}
	
	public InvolvedPartyXRules setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}
	
	public InvolvedPartyXRules getInvolvedPartyXRules()
	{
		return this.involvedPartyXRules;
	}
	
	public InvolvedPartyXRules setInvolvedPartyXRules(InvolvedPartyXRules involvedPartyXRules)
	{
		this.involvedPartyXRules = involvedPartyXRules;
		return this;
	}
	
	public InvolvedPartyXRules getInvolvedPartyXRules1()
	{
		return this.involvedPartyXRules1;
	}
	
	public InvolvedPartyXRules setInvolvedPartyXRules1(InvolvedPartyXRules involvedPartyXRules1)
	{
		this.involvedPartyXRules1 = involvedPartyXRules1;
		return this;
	}
	
	public Rules getRulesID()
	{
		return this.rulesID;
	}
	
	public InvolvedPartyXRules setRulesID(Rules rulesID)
	{
		this.rulesID = rulesID;
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
		InvolvedPartyXRules that = (InvolvedPartyXRules) o;
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
	public Rules getSecondary()
	{
		return getRulesID();
	}
}
