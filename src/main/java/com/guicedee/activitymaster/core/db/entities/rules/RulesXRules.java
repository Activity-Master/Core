package com.guicedee.activitymaster.core.db.entities.rules;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesXRulesQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IRules;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Rules",
		name = "RulesXRules")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
public class RulesXRules
		extends WarehouseClassificationRelationshipTable<Rules,
				                                                Rules,
				                                                RulesXRules,
				                                                RulesXRulesQueryBuilder,
				                                                Long,
				                                                RulesXRulesSecurityToken,
				                                                IRules<?>, IRules<?>>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "RulesXRulesID")
	private Long id;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<RulesXRulesSecurityToken> securities;

	@JoinColumn(name = "ChildRulesID",
			referencedColumnName = "RulesID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Rules childRulesID;
	@JoinColumn(name = "ParentRulesID",
			referencedColumnName = "RulesID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Rules parentRulesID;

	public RulesXRules()
	{

	}

	public RulesXRules(Long rulesXRulesID)
	{
		id = rulesXRulesID;
	}

	@Override
	protected RulesXRulesSecurityToken configureDefaultsForNewToken(RulesXRulesSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public Long getId()
	{
		return id;
	}

	public List<RulesXRulesSecurityToken> getSecurities()
	{
		return securities;
	}

	public Rules getChildRulesID()
	{
		return childRulesID;
	}

	public Rules getParentRulesID()
	{
		return parentRulesID;
	}

	@Override
	public RulesXRules setId(Long id)
	{
		this.id = id;
		return this;
	}

	public RulesXRules setSecurities(List<RulesXRulesSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public RulesXRules setChildRulesID(Rules childRulesID)
	{
		this.childRulesID = childRulesID;
		return this;
	}

	public RulesXRules setParentRulesID(Rules parentRulesID)
	{
		this.parentRulesID = parentRulesID;
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
		RulesXRules that = (RulesXRules) o;
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
		return getParentRulesID();
	}

	@Override
	public IRules<?> getSecondary()
	{
		return getChildRulesID();
	}
}
