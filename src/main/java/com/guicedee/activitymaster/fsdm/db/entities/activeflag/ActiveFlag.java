package com.guicedee.activitymaster.fsdm.db.entities.activeflag;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.assists.WarehouseNameDescriptionTable;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders.ActiveFlagQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serial;
import java.sql.Types;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ActiveFlag",
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
public class ActiveFlag
		extends WarehouseNameDescriptionTable<ActiveFlag, ActiveFlagQueryBuilder, java.lang.String>
		implements IActiveFlag<ActiveFlag, ActiveFlagQueryBuilder>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
	        name = "ActiveFlagID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "EnterpriseID",
	            referencedColumnName = "EnterpriseID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Enterprise enterpriseID;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
	      max = 100)
	@Column(nullable = false,
	        length = 100,
	        name = "ActiveFlagName")
	private String name;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
	      max = 100)
	@Column(nullable = false,
	        length = 100,
	        name = "ActiveFlagDescription")
	private String description;
	
	@Basic(optional = false,
	       fetch = FetchType.LAZY)
	@NotNull
	@Column(nullable = false,
	        name = "AllowAccess")
	@JdbcTypeCode(Types.INTEGER)
	private boolean allowAccess;
	
	@SuppressWarnings("unused")
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ActiveFlagSecurityToken> securities;
	
	public ActiveFlag()
	{
	
	}
	
	public ActiveFlag(java.lang.String id)
	{
		this.id = id;
	}
	
	public ActiveFlag(java.lang.String id, String activeFlagName, boolean allowAccess)
	{
		this.id = id;
		name = activeFlagName;
		this.allowAccess = allowAccess;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getName());
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
		ActiveFlag that = (ActiveFlag) o;
		return getName().equals(that.getName());
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	@Override
	public java.lang.String getId()
	{
		return id;
	}
	
	
	@Override
	public ActiveFlag setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public Enterprise getEnterpriseID()
	{
		return enterpriseID;
	}
	
	public ActiveFlag setEnterpriseID(IEnterprise<?, ?> enterpriseID)
	{
		this.enterpriseID = (Enterprise) enterpriseID;
		return this;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public ActiveFlag setName(String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	public String getDescription()
	{
		return description;
	}
	
	@Override
	public ActiveFlag setDescription(String description)
	{
		this.description = description;
		return this;
	}
	
	public boolean isAllowAccess()
	{
		return allowAccess;
	}
	
	public ActiveFlag setAllowAccess(boolean allowAccess)
	{
		this.allowAccess = allowAccess;
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable, IClassification<?, ?> classificationValue, ISystems<?, ?> system)
	{
		ActiveFlagXClassification x = (ActiveFlagXClassification) linkTable;
		x.setActiveFlagID(this);
	}
}
