package com.guicedee.activitymaster.core.db.entities.arrangement;


import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementXRulesTypeQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.rules.RulesType;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Arrangement", name = "ArrangementXRulesType")
@XmlRootElement
@Access(FIELD)
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
				UUID>
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementXRulesTypeID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
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
	
	public ArrangementXRulesType(UUID arrangementXRulesID)
	{
		this.id = arrangementXRulesID;
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public List<ArrangementXRulesTypeSecurityToken> getArrangementXRulesTypeSecurityTokenList()
	{
		return this.arrangementXRulesSecurityTokenList;
	}
	
	public Arrangement getArrangement()
	{
		return this.arrangement;
	}
	

	public List<ArrangementXRulesTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public ArrangementXRulesType setId(UUID id)
	{
		this.id = id;
		return this;
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
	
	public RulesType getRulesTypeID()
	{
		return rulesTypeID;
	}
	
	public void setRulesTypeID(RulesType rulesTypeID)
	{
		this.rulesTypeID = rulesTypeID;
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
		return Objects.hash(getId());
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
