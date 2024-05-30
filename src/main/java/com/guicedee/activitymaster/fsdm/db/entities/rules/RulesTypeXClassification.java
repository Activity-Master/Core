package com.guicedee.activitymaster.fsdm.db.entities.rules;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.rules.builders.RulesTypeXClassificationQueryBuilder;
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
@Table(schema = "Rules", name = "RulesTypeXClassification")
@XmlRootElement

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
		java.lang.String, RulesTypeXClassificationSecurityToken>
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesTypeXClassificationID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
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
	
	public RulesTypeXClassification(java.lang.String rulesXClassificationID)
	{
		this.id = rulesXClassificationID;
	}
	
	@Override
	public void configureSecurityEntity(RulesTypeXClassificationSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public RulesTypeXClassification setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public RulesType getRulesTypeID()
	{
		return this.rulesTypeID;
	}
	
	public RulesTypeXClassification setRulesTypeID(RulesType rulesTypeID)
	{
		this.rulesTypeID = rulesTypeID;
		return this;
	}
	
	public List<RulesTypeXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
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
