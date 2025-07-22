package com.guicedee.activitymaster.fsdm.db.entities.arrangement;


import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXRulesQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.rules.Rules;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Arrangement", name = "ArrangementXRules")
@XmlRootElement
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArrangementXRules
		extends WarehouseClassificationRelationshipTable<Arrangement,
                                Rules,
                                ArrangementXRules,
                                ArrangementXRulesQueryBuilder,
                                UUID,
                                ArrangementXRulesSecurityToken
                                >
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementXRulesID")

	private java.util.UUID id;
	
@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<ArrangementXRulesSecurityToken> arrangementXRulesSecurityTokenList;
	
	@JoinColumn(name = "ArrangementID",
	            referencedColumnName = "ArrangementID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Arrangement arrangement;
	
	@JoinColumn(name = "RulesID",
	            referencedColumnName = "RulesID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Rules rulesID;
	
@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<ArrangementXRulesSecurityToken> securities;

	@Override
	public void configureSecurityEntity(ArrangementXRulesSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}

	
	public ArrangementXRules setArrangementXRulesSecurityTokenList(List<ArrangementXRulesSecurityToken> arrangementXRulesSecurityTokenList)
	{
		this.arrangementXRulesSecurityTokenList = arrangementXRulesSecurityTokenList;
		return this;
	}
	
	public ArrangementXRules setArrangement(Arrangement arrangement)
	{
		this.arrangement = arrangement;
		return this;
	}
	
	public ArrangementXRules setRulesID(Rules resourceItemID)
	{
		this.rulesID = resourceItemID;
		return this;
	}
	
	public ArrangementXRules setSecurities(List<ArrangementXRulesSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	@Override
	public Arrangement getPrimary()
	{
		return getArrangement();
	}
	
	@Override
	public Rules getSecondary()
	{
		return getRulesID();
	}

	public List<ArrangementXRulesSecurityToken> getArrangementXRulesSecurityTokenList()
	{
		return arrangementXRulesSecurityTokenList;
	}
	
	public Arrangement getArrangement()
	{
		return arrangement;
	}
	
	public Rules getRulesID()
	{
		return rulesID;
	}
	
	public List<ArrangementXRulesSecurityToken> getSecurities()
	{
		return securities;
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
		ArrangementXRules that = (ArrangementXRules) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
