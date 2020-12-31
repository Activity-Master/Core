package com.guicedee.activitymaster.core.db.entities.activeflag;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.activeflag.builders.ActiveFlagXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.IActiveFlag;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
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
@Table(name = "ActiveFlagXClassification")
@XmlRootElement

@Access(FIELD)
public class ActiveFlagXClassification
		extends WarehouseClassificationRelationshipTable<ActiveFlag,
				                                                Classification,
						                                                ActiveFlagXClassification,
				                                                ActiveFlagXClassificationQueryBuilder,
						                                                java.util.UUID,
						                                                ActiveFlagXClassificationSecurityToken,
				                                                IActiveFlag<?>, IClassification<?>>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
			name = "ActiveFlagXClassificationID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	@JoinColumn(name = "SystemID",
			referencedColumnName = "SystemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Systems systemID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ActiveFlagXClassificationSecurityToken> securities;

	public ActiveFlagXClassification()
	{

	}

	public ActiveFlagXClassification(UUID activeFlagXClassificationID)
	{
		this.id = activeFlagXClassificationID;
	}

	@Override
	protected ActiveFlagXClassificationSecurityToken configureDefaultsForNewToken(ActiveFlagXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public Systems getSystemID()
	{
		return this.systemID;
	}

	public List<ActiveFlagXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public ActiveFlagXClassification setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public ActiveFlagXClassification setSystemID(Systems systemID)
	{
		this.systemID = systemID;
		return this;
	}

	public ActiveFlagXClassification setSecurities(List<ActiveFlagXClassificationSecurityToken> securities)
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
		ActiveFlagXClassification that = (ActiveFlagXClassification) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IActiveFlag<?> getPrimary()
	{
		return getActiveFlagID();
	}

	@Override
	public IClassification<?> getSecondary()
	{
		return getClassificationID();
	}
}
