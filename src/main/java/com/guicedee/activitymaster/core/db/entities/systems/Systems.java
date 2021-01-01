package com.guicedee.activitymaster.core.db.entities.systems;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.builders.SystemsQueryBuilder;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.systems.ISystemsClassification;
import com.guicedee.activitymaster.core.services.dto.IActiveFlag;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.systems.ActiveFlagSystem;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.guicedee.guicedinjection.GuiceContext.*;
import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "Systems")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
public class Systems
		extends WarehouseNameDescriptionTable<Systems, SystemsQueryBuilder, java.util.UUID, SystemsSecurityToken>
		implements IContainsClassifications<Systems, Classification, SystemXClassification, ISystemsClassification<?>, ISystems<?>, IClassification<?>, Systems>,
		           IActivityMasterEntity<Systems>,
		           IContainsNameAndDescription<Systems>,
		           IContainsEnterprise<Systems>,
		           ISystems<Systems>,
		           IContainsActiveFlags<Systems>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "SystemID")
	@JsonValue
	@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	@Basic(optional = false,
	       fetch = FetchType.EAGER)
	@NotNull
	@Size(min = 1,
	      max = 150)
	@Column(nullable = false,
	        length = 150,
	        name = "SystemName")
	@JsonIgnore
	private String name;
	@Basic(optional = false,
	       fetch = FetchType.EAGER)
	@NotNull
	@Size()
	@Column(nullable = false,
	        length = 250,
	        name = "SystemDesc")
	@JsonIgnore
	private String description;
	@Basic(optional = false,
	       fetch = FetchType.EAGER)
	@NotNull
	@Size(min = 1,
	      max = 250)
	@Column(nullable = false,
	        length = 250,
	        name = "SystemHistoryName")
	@JsonIgnore
	private String systemHistoryName;
	
	@JoinColumn(name = "EnterpriseID",
	            referencedColumnName = "EnterpriseID",
	            nullable = false)
	@ManyToOne(optional = false)
	@JsonIgnore
	private Enterprise enterpriseID;
	
	@JoinColumn(name = "ActiveFlagID",
	            referencedColumnName = "ActiveFlagID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.EAGER)
	@JsonIgnore
	private ActiveFlag activeFlagID;
	
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<SystemsSecurityToken> securities;
	
	public Systems()
	{
	
	}
	
	public Systems(UUID systemID)
	{
		id = systemID;
	}
	
	public Systems(UUID systemID, String systemName, String systemDesc, String systemHistoryName)
	{
		id = systemID;
		name = systemName;
		description = systemDesc;
		this.systemHistoryName = systemHistoryName;
	}
	
	@Override
	protected SystemsSecurityToken configureDefaultsForNewToken(SystemsSecurityToken stAdmin, ISystems<?> system, ISystems<?> activityMasterSystem)
	{
		SystemsSecurityToken token = super.configureDefaultsForNewToken(stAdmin, system, activityMasterSystem);
		stAdmin.setSystemID(this);
		return token;
	}
	
	@Override
	public String toString()
	{
		return "System - " + getName();
	}
	
	@Override
	public void configureForClassification(SystemXClassification classificationLink, ISystems<?> system)
	{
		classificationLink.setSystemID(this);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Systems remove()
	{
		setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
		                                         .getDeletedFlag(getEnterpriseID(), get(ActiveFlagSystem.class).getSystemToken(getEnterpriseID())));
		setEffectiveToDate(LocalDateTime.now());
		updateNow();
		return this;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Systems archive()
	{
		setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
		                                         .getArchivedFlag(getEnterpriseID(), get(ActiveFlagSystem.class).getSystemToken(getEnterpriseID())));
		setEffectiveToDate(LocalDateTime.now());
		updateNow();
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
		Systems systems = (Systems) o;
		return Objects.equals(getName(), systems.getName());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public java.util.UUID getId()
	{
		return id;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public @NotNull @Size() String getDescription()
	{
		return description;
	}
	
	public String getSystemHistoryName()
	{
		return systemHistoryName;
	}
	
	@Override
	public Enterprise getEnterpriseID()
	{
		return enterpriseID;
	}
	
	public ActiveFlag getActiveFlagID()
	{
		return activeFlagID;
	}
	
	@Override
	public Systems setActiveFlagID(IActiveFlag<?> activeFlagID)
	{
		return setActiveFlagID((ActiveFlag) activeFlagID);
	}
	
	@Override
	public Systems setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public Systems setName(String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	public Systems setDescription(@NotNull @Size() String description)
	{
		this.description = description;
		return this;
	}
	
	public Systems setSystemHistoryName(@NotNull String systemHistoryName)
	{
		this.systemHistoryName = systemHistoryName;
		return this;
	}
	
	public Systems setEnterpriseID(Enterprise enterpriseID)
	{
		this.enterpriseID = enterpriseID;
		return this;
	}
	
	public Systems setActiveFlagID(ActiveFlag activeFlagID)
	{
		this.activeFlagID = activeFlagID;
		return this;
	}
}
