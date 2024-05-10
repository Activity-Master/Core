package com.guicedee.activitymaster.fsdm.db.entities.arrangement;


import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXRulesQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.rules.Rules;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Getter
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
@EqualsAndHashCode(of = "id", callSuper = false)
public class ArrangementXRules
		extends WarehouseClassificationRelationshipTable<Arrangement,
		Rules,
		ArrangementXRules,
		ArrangementXRulesQueryBuilder,
		java.lang.String,
		ArrangementXRulesSecurityToken
		>
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementXRulesID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
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
			fetch = FetchType.LAZY)
	private List<ArrangementXRulesSecurityToken> securities;
	
	public ArrangementXRules()
	{
	
	}
	
	public ArrangementXRules(java.lang.String arrangementXRulesID)
	{
		this.id = arrangementXRulesID;
	}
	
	public ArrangementXRules setId(java.lang.String id)
	{
		this.id = id;
		return this;
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
}
