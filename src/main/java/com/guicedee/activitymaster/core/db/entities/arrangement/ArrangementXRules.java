package com.guicedee.activitymaster.core.db.entities.arrangement;


import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementXRulesQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import com.guicedee.activitymaster.core.services.dto.IArrangement;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IRules;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.AccessType.FIELD;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Arrangement", name = "ArrangementXRules")
@XmlRootElement

@Access(FIELD)
public class ArrangementXRules
		extends WarehouseClassificationRelationshipTable<Arrangement,
		Rules,
		ArrangementXRules,
		ArrangementXRulesQueryBuilder,
		UUID,
		ArrangementXRulesSecurityToken,
		IArrangement<?>, IRules<?>>
		implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementXRulesID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
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
	
	public ArrangementXRules(UUID arrangementXRulesID)
	{
		this.id = arrangementXRulesID;
	}
	
	@Override
	protected ArrangementXRulesSecurityToken configureDefaultsForNewToken(ArrangementXRulesSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public List<ArrangementXRulesSecurityToken> getArrangementXRulesSecurityTokenList()
	{
		return this.arrangementXRulesSecurityTokenList;
	}
	
	public Arrangement getArrangement()
	{
		return this.arrangement;
	}
	
	public Rules getRulesID()
	{
		return this.rulesID;
	}
	
	public List<ArrangementXRulesSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public ArrangementXRules setId(UUID id)
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
		return Objects.hash(getId());
	}
	
	@Override
	public IArrangement<?> getPrimary()
	{
		return getArrangement();
	}
	
	@Override
	public IRules<?> getSecondary()
	{
		return getRulesID();
	}
}
