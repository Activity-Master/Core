package com.guicedee.activitymaster.fsdm.db.entities.arrangement;


import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXRulesTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesType;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Arrangement", name = "ArrangementXRulesType")
@XmlRootElement
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@EqualsAndHashCode(of = "id", callSuper = false)
public class ArrangementXRulesType
		extends WarehouseClassificationRelationshipTable<Arrangement,
		RulesType,
		ArrangementXRulesType,
		ArrangementXRulesTypeQueryBuilder,
		java.lang.String,
		ArrangementXRulesTypeSecurityToken
		>
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Getter
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementXRulesTypeID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementXRulesTypeSecurityToken> arrangementXRulesSecurityTokenList;
	
	@Getter
	@JoinColumn(name = "ArrangementID",
	            referencedColumnName = "ArrangementID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Arrangement arrangement;
	
	@Setter
	@Getter
	@JoinColumn(name = "RulesTypeID",
	            referencedColumnName = "RulesTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private RulesType rulesTypeID;
	
	@Getter
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementXRulesTypeSecurityToken> securities;
	
	public ArrangementXRulesType()
	{
	
	}
	
	public ArrangementXRulesType(java.lang.String arrangementXRulesID)
	{
		this.id = arrangementXRulesID;
	}
	
	public ArrangementXRulesType setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public List<ArrangementXRulesTypeSecurityToken> getArrangementXRulesTypeSecurityTokenList()
	{
		return this.arrangementXRulesSecurityTokenList;
	}
	
	public ArrangementXRulesType setArrangementXRulesTypeSecurityTokenList(List<ArrangementXRulesTypeSecurityToken> arrangementXRulesSecurityTokenList)
	{
		this.arrangementXRulesSecurityTokenList = arrangementXRulesSecurityTokenList;
		return this;
	}
	
	public ArrangementXRulesType setArrangement(Arrangement arrangement)
	{
		this.arrangement = arrangement;
		return this;
	}
	
	public ArrangementXRulesType setSecurities(List<ArrangementXRulesTypeSecurityToken> securities)
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
	public RulesType getSecondary()
	{
		return getRulesTypeID();
	}
}
