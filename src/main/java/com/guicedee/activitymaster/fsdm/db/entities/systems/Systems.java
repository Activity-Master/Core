package com.guicedee.activitymaster.fsdm.db.entities.systems;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.capabilities.contains.IContainsNameAndDescription;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.systems.builders.SystemsQueryBuilder;
import com.guicedee.activitymaster.fsdm.systems.ActiveFlagSystem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static com.guicedee.client.IGuiceContext.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "Systems",
       schema = "dbo")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class Systems
		extends WarehouseCoreTable<Systems, SystemsQueryBuilder, String, SystemsSecurityToken>
		implements ISystems<Systems, SystemsQueryBuilder>,
		           IContainsNameAndDescription<Systems>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
	        name = "SystemID")
	@JsonValue
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
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
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<SystemsSecurityToken> securities;
	
	public Systems()
	{
	
	}
	
	public Systems(java.lang.String systemID)
	{
		id = systemID;
	}
	
	public Systems(java.lang.String systemID, String systemName, String systemDesc, String systemHistoryName)
	{
		id = systemID;
		name = systemName;
		description = systemDesc;
		this.systemHistoryName = systemHistoryName;
	}
	
	@Override
	public void configureSecurityEntity(SystemsSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	
	public void configureForClassification(SystemsXClassification classificationLink, ISystems<?, ?> system)
	{
		classificationLink.setSystemID(this);
	}
	
	public Systems remove()
	{
		setActiveFlagID((ActiveFlag) com.guicedee.client.IGuiceContext.get(IActiveFlagService.class)
		                                         .getDeletedFlag(getEnterpriseID(), get(ActiveFlagSystem.class).getSystemToken(getEnterpriseID())));
		setEffectiveToDate(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		update();
		return this;
	}
	
	
	public Systems archive()
	{
		setActiveFlagID((ActiveFlag) com.guicedee.client.IGuiceContext.get(IActiveFlagService.class)
		                                         .getArchivedFlag(getEnterpriseID(), get(ActiveFlagSystem.class).getSystemToken(getEnterpriseID())));
		update();
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
	public java.lang.String getId()
	{
		return id;
	}
	
	@Override
	public Systems setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public Systems setName(String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	public @NotNull @Size() String getDescription()
	{
		return description;
	}
	
	@Override
	public Systems setDescription(@NotNull @Size() String description)
	{
		this.description = description;
		return this;
	}
	
	public String getSystemHistoryName()
	{
		return systemHistoryName;
	}
	
	public Systems setSystemHistoryName(@NotNull String systemHistoryName)
	{
		this.systemHistoryName = systemHistoryName;
		return this;
	}
	
	@Override
	public Enterprise getEnterpriseID()
	{
		return enterpriseID;
	}
	
	public Systems setEnterpriseID(IEnterprise<?, ?> enterpriseID)
	{
		this.enterpriseID = (Enterprise) enterpriseID;
		return this;
	}
	
	public ActiveFlag getActiveFlagID()
	{
		return activeFlagID;
	}
	
	@Override
	public Systems setActiveFlagID(IActiveFlag<?, ?> activeFlagID)
	{
		return setActiveFlagID((ActiveFlag) activeFlagID);
	}
	
	public Systems setActiveFlagID(ActiveFlag activeFlagID)
	{
		this.activeFlagID = activeFlagID;
		return this;
	}
	
	@Override
	public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable, IClassification<?, ?> classificationValue, ISystems<?, ?> system)
	{
		((SystemsXClassification) linkTable)
		                            .setSystemID(this);
	}
}
