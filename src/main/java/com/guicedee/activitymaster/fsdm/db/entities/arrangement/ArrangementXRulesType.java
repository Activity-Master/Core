package com.guicedee.activitymaster.fsdm.db.entities.arrangement;


import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXRulesTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesType;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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

	@Id
	
	@Column(nullable = false,
	        name = "ArrangementXRulesTypeID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementXRulesTypeSecurityToken> arrangementXRulesSecurityTokenList;
	
	@JoinColumn(name = "ArrangementID",
	            referencedColumnName = "ArrangementID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Arrangement arrangement;
	
	@JoinColumn(name = "RulesTypeID",
	            referencedColumnName = "RulesTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private RulesType rulesTypeID;
	
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
	
	@Override
	public void configureSecurityEntity(ArrangementXRulesTypeSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
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
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public List<ArrangementXRulesTypeSecurityToken> getArrangementXRulesSecurityTokenList()
	{
		return arrangementXRulesSecurityTokenList;
	}
	
	public ArrangementXRulesType setArrangementXRulesSecurityTokenList(List<ArrangementXRulesTypeSecurityToken> arrangementXRulesSecurityTokenList)
	{
		this.arrangementXRulesSecurityTokenList = arrangementXRulesSecurityTokenList;
		return this;
	}
	
	public Arrangement getArrangement()
	{
		return arrangement;
	}
	
	public RulesType getRulesTypeID()
	{
		return rulesTypeID;
	}
	
	public ArrangementXRulesType setRulesTypeID(RulesType rulesTypeID)
	{
		this.rulesTypeID = rulesTypeID;
		return this;
	}
	
	public List<ArrangementXRulesTypeSecurityToken> getSecurities()
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
		ArrangementXRulesType that = (ArrangementXRulesType) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
