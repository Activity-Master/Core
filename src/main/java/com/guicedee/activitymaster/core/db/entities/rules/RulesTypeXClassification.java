package com.guicedee.activitymaster.core.db.entities.rules;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.rules.RulesType;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesTypeXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IRulesType;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
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
@Table(schema = "Rules", name = "RulesTypeXClassification")
@XmlRootElement

@Access(FIELD)
public class RulesTypeXClassification
		extends WarehouseClassificationRelationshipTable<RulesType,
		Classification,
		RulesTypeXClassification,
		RulesTypeXClassificationQueryBuilder,
		UUID,
		RulesTypeXClassificationSecurityToken,
		IRulesType<?>, IClassification<?>>
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
	
	@Override
	protected RulesTypeXClassificationSecurityToken configureDefaultsForNewToken(RulesTypeXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
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
	public IRulesType<?> getPrimary()
	{
		return getRulesTypeID();
	}
	
	@Override
	public IClassification<?> getSecondary()
	{
		return getSecondary();
	}
}
