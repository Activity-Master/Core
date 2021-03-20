package com.guicedee.activitymaster.core.db.entities.rules;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesXArrangementQueryBuilder;
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
@Table(schema = "Rules", name = "RulesXArrangement")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class RulesXArrangement
		extends WarehouseClassificationRelationshipTable<Rules,
		Arrangement,
		RulesXArrangement,
		RulesXArrangementQueryBuilder,
		java.util.UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesXArrangementsID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<RulesXArrangementsSecurityToken> securities;
	
	@JoinColumn(name = "ArrangementID",
	            referencedColumnName = "ArrangementID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Arrangement arrangementID;
	
	@JoinColumn(name = "RulesID",
	            referencedColumnName = "RulesID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Rules rulesID;
	
	public RulesXArrangement()
	{
	
	}
	
	public RulesXArrangement(UUID RulesXArrangementsID)
	{
		this.id = RulesXArrangementsID;
	}
	
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	public List<RulesXArrangementsSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public Arrangement getArrangementID()
	{
		return this.arrangementID;
	}
	
	public Rules getRulesID()
	{
		return this.rulesID;
	}
	
	public RulesXArrangement setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public RulesXArrangement setSecurities(List<RulesXArrangementsSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public RulesXArrangement setArrangementID(Arrangement arrangementID)
	{
		this.arrangementID = arrangementID;
		return this;
	}
	
	public RulesXArrangement setRulesID(Rules RulesID)
	{
		this.rulesID = RulesID;
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
		RulesXArrangement that = (RulesXArrangement) o;
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
	public Arrangement getSecondary()
	{
		return getArrangementID();
	}
}
