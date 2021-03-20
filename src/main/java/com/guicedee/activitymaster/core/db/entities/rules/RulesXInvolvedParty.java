package com.guicedee.activitymaster.core.db.entities.rules;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesXInvolvedPartyQueryBuilder;
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
@Table(schema = "Rules", name = "RulesXInvolvedParty")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class RulesXInvolvedParty
		extends WarehouseClassificationRelationshipTable<Rules,
		InvolvedParty,
		RulesXInvolvedParty,
		RulesXInvolvedPartyQueryBuilder,
		java.util.UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesXInvolvedPartyID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
	@JoinColumn(name = "RulesID",
	            referencedColumnName = "RulesID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Rules rulesID;
	@JoinColumn(name = "InvolvedPartyID",
	            referencedColumnName = "InvolvedPartyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private InvolvedParty involvedPartyID;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<RulesXInvolvedPartySecurityToken> securities;
	
	public RulesXInvolvedParty()
	{
	
	}
	
	public RulesXInvolvedParty(UUID RulesXInvolvedPartyID)
	{
		this.id = RulesXInvolvedPartyID;
	}
	
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	public Rules getRulesID()
	{
		return this.rulesID;
	}
	
	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}
	
	public List<RulesXInvolvedPartySecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public RulesXInvolvedParty setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public RulesXInvolvedParty setRulesID(Rules RulesID)
	{
		this.rulesID = RulesID;
		return this;
	}
	
	public RulesXInvolvedParty setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}
	
	public RulesXInvolvedParty setSecurities(List<RulesXInvolvedPartySecurityToken> securities)
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
		RulesXInvolvedParty that = (RulesXInvolvedParty) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public Rules getPrimary()
	{
		return getRulesID();
	}
	
	@Override
	public InvolvedParty getSecondary()
	{
		return getInvolvedPartyID();
	}
}
