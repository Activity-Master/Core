package com.guicedee.activitymaster.core.db.entities.rules;


import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesXRulesTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IRules;
import com.guicedee.activitymaster.core.services.dto.IRulesType;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
@Access(FIELD)
public class RulesXRulesType
		extends WarehouseClassificationRelationshipTable<Rules,
		RulesType,
		RulesXRulesType,
		RulesXRulesTypeQueryBuilder,
		java.util.UUID,
		RulesXRulesTypeSecurityToken,
		IRules<?>, IRulesType<?>>
{
	
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
	protected RulesXRulesTypeSecurityToken configureDefaultsForNewToken(RulesXRulesTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
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
	public IRules<?> getPrimary()
	{
		return getRulesID();
	}
	
	@Override
	public IRulesType<?> getSecondary()
	{
		return getRulesTypeID();
	}
}
