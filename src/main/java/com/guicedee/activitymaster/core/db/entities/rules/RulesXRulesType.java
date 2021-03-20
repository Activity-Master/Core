package com.guicedee.activitymaster.core.db.entities.rules;


import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesXRulesTypeQueryBuilder;
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
@Table(schema = "Rules",
       name = "RulesXRulesType")
@XmlRootElement
@Access(FIELD)@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class RulesXRulesType
		extends WarehouseClassificationRelationshipTable<Rules,
		RulesType,
		RulesXRulesType,
		RulesXRulesTypeQueryBuilder,
		java.util.UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
	        name = "RulesXRulesTypeID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<RulesXRulesTypeSecurityToken> securities;
	@JoinColumn(name = "RulesID",
	            referencedColumnName = "RulesID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Rules rulesID;
	@JoinColumn(name = "RulesTypeID",
	            referencedColumnName = "RulesTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private RulesType rulesTypeID;
	
	public RulesXRulesType()
	{
	
	}
	
	public RulesXRulesType(UUID rulesXRulesTypeID)
	{
		this.id = rulesXRulesTypeID;
	}
	
	@Override
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	public List<RulesXRulesTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public Rules getRulesID()
	{
		return this.rulesID;
	}
	
	public RulesType getRulesTypeID()
	{
		return this.rulesTypeID;
	}
	
	@Override
	public RulesXRulesType setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public RulesXRulesType setSecurities(List<RulesXRulesTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public RulesXRulesType setRulesID(Rules rulesID)
	{
		this.rulesID = rulesID;
		return this;
	}
	
	public RulesXRulesType setRulesTypeID(RulesType rulesTypeID)
	{
		this.rulesTypeID = rulesTypeID;
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
		RulesXRulesType that = (RulesXRulesType) o;
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
	public RulesType getSecondary()
	{
		return getRulesTypeID();
	}
}
