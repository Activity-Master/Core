/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyXRulesQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
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
@Table(schema = "Party", name = "InvolvedPartyXRules")
@XmlRootElement

@Access(FIELD)
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
		UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyXRulesID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
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
	
	public InvolvedPartyXRules(UUID involvedPartyXRulesID)
	{
		this.id = involvedPartyXRulesID;
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public List<InvolvedPartyXRulesSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}
	
	public InvolvedPartyXRules getInvolvedPartyXRules()
	{
		return this.involvedPartyXRules;
	}
	
	public InvolvedPartyXRules getInvolvedPartyXRules1()
	{
		return this.involvedPartyXRules1;
	}
	
	public Rules getRulesID()
	{
		return this.rulesID;
	}
	
	public InvolvedPartyXRules setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedPartyXRules setSecurities(List<InvolvedPartyXRulesSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public InvolvedPartyXRules setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}
	
	public InvolvedPartyXRules setInvolvedPartyXRules(InvolvedPartyXRules involvedPartyXRules)
	{
		this.involvedPartyXRules = involvedPartyXRules;
		return this;
	}
	
	public InvolvedPartyXRules setInvolvedPartyXRules1(InvolvedPartyXRules involvedPartyXRules1)
	{
		this.involvedPartyXRules1 = involvedPartyXRules1;
		return this;
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
