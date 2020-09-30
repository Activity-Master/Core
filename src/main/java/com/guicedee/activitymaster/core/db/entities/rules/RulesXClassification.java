package com.guicedee.activitymaster.core.db.entities.rules;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IRules;
import com.guicedee.activitymaster.core.services.dto.ISystems;

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
@Table(schema="Rules",name = "RulesXClassification")
@XmlRootElement

@Access(FIELD)
public class RulesXClassification
		extends WarehouseClassificationRelationshipTable<Rules,
				                                                Classification,
				                                                RulesXClassification,
				                                                RulesXClassificationQueryBuilder,
				                                                Long,
				                                                RulesXClassificationSecurityToken,
				                                                IRules<?>, IClassification<?>>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "RulesXClassificationID")
	private Long id;

	@JoinColumn(name = "RulesID",
			referencedColumnName = "RulesID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Rules rulesID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<RulesXClassificationSecurityToken> securities;

	public RulesXClassification()
	{

	}

	public RulesXClassification(Long rulesXClassificationID)
	{
		this.id = rulesXClassificationID;
	}

	@Override
	protected RulesXClassificationSecurityToken configureDefaultsForNewToken(RulesXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
	{
		return this.id;
	}

	public Rules getRulesID()
	{
		return this.rulesID;
	}

	public List<RulesXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public RulesXClassification setId(Long id)
	{
		this.id = id;
		return this;
	}

	public RulesXClassification setRulesID(Rules rulesID)
	{
		this.rulesID = rulesID;
		return this;
	}

	public RulesXClassification setSecurities(List<RulesXClassificationSecurityToken> securities)
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
		RulesXClassification that = (RulesXClassification) o;
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
	public IClassification<?> getSecondary()
	{
		return getSecondary();
	}
}
