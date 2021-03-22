package com.guicedee.activitymaster.core.db.entities.systems;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.client.services.IActiveFlagService;
import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.builders.SystemsQueryBuilder;
import com.guicedee.activitymaster.core.systems.ActiveFlagSystem;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class Systems
		extends WarehouseNameDescriptionTable<Systems, SystemsQueryBuilder, java.util.UUID>
		implements ISystems<Systems, SystemsQueryBuilder>
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
	private String name;
	@Basic(optional = false,
	       fetch = FetchType.EAGER)
	@NotNull
	@Size()
	@Column(nullable = false,
	        length = 250,
	        name = "SystemDesc")
	private String description;
	@Basic(optional = false,
	       fetch = FetchType.EAGER)
	@NotNull
	@Size(min = 1,
	      max = 250)
	@Column(nullable = false,
	        length = 250,
	        name = "SystemHistoryName")
	private String systemHistoryName;
	
	@JoinColumn(name = "EnterpriseID",
	            referencedColumnName = "EnterpriseID",
	            nullable = false)
	@ManyToOne(optional = false)
	private Enterprise enterpriseID;
	
	@JoinColumn(name = "ActiveFlagID",
	            referencedColumnName = "ActiveFlagID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.EAGER)
	private ActiveFlag activeFlagID;
	
	@OneToMany(
			mappedBy = "systemID",
			fetch = FetchType.LAZY)
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
	public String toString()
	{
		return getName();
	}
	
	
	public void configureForClassification(SystemsXClassification classificationLink, ISystems<?,?> system)
	{
		classificationLink.setSystemID(this);
	}
	
	public Systems remove()
	{
		setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
		                                         .getDeletedFlag(getEnterpriseID(), get(ActiveFlagSystem.class).getSystemToken(getEnterpriseID())));
		setEffectiveToDate(LocalDateTime.now());
		updateNow();
		return this;
	}
	
	
	public Systems archive()
	{
		setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
		                                         .getArchivedFlag(getEnterpriseID(), get(ActiveFlagSystem.class).getSystemToken(getEnterpriseID())));
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
		return Objects.hash(getName());
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
	public Systems setActiveFlagID(IActiveFlag<?,?> activeFlagID)
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
	
	public Systems setEnterpriseID(IEnterprise<?,?> enterpriseID)
	{
		this.enterpriseID = (Enterprise) enterpriseID;
		return this;
	}
	
	public Systems setActiveFlagID(ActiveFlag activeFlagID)
	{
		this.activeFlagID = activeFlagID;
		return this;
	}
	
	@Override
	public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable, IClassification<?,?> classificationValue, ISystems<?,?> system)
	{
		SystemsXClassification.class.cast(linkTable)
		                            .setSystemID(this);
	}
}
