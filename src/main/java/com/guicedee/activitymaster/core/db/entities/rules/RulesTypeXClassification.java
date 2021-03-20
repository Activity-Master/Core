package com.guicedee.activitymaster.core.db.entities.rules;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesTypeXClassificationQueryBuilder;
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
@Table(schema = "Rules", name = "RulesTypeXClassification")
@XmlRootElement

@Access(FIELD)@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class RulesTypeXClassification
		extends WarehouseClassificationRelationshipTable<RulesType,
		Classification,
		RulesTypeXClassification,
		RulesTypeXClassificationQueryBuilder,
		UUID>
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesTypeXClassificationID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
	@JoinColumn(name = "RulesTypeID",
	            referencedColumnName = "RulesTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private RulesType rulesTypeID;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<RulesTypeXClassificationSecurityToken> securities;
	
	public RulesTypeXClassification()
	{
	
	}
	
	public RulesTypeXClassification(UUID rulesXClassificationID)
	{
		this.id = rulesXClassificationID;
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public RulesType getRulesTypeID()
	{
		return this.rulesTypeID;
	}
	
	public List<RulesTypeXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public RulesTypeXClassification setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public RulesTypeXClassification setRulesTypeID(RulesType rulesTypeID)
	{
		this.rulesTypeID = rulesTypeID;
		return this;
	}
	
	public RulesTypeXClassification setSecurities(List<RulesTypeXClassificationSecurityToken> securities)
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
		RulesTypeXClassification that = (RulesTypeXClassification) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public RulesType getPrimary()
	{
		return getRulesTypeID();
	}
	
	@Override
	public Classification getSecondary()
	{
		return getSecondary();
	}
}
