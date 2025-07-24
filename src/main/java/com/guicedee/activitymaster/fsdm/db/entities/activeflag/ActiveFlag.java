package com.guicedee.activitymaster.fsdm.db.entities.activeflag;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders.ActiveFlagQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.reactive.mutiny.Mutiny;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActiveFlag
		extends WarehouseCoreTable<ActiveFlag, ActiveFlagQueryBuilder, UUID, ActiveFlagSecurityToken>
		implements IActiveFlag<ActiveFlag, ActiveFlagQueryBuilder>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
	        name = "ActiveFlagID")
	
	private java.util.UUID id;
	
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
	private boolean allowAccess;
	
	@SuppressWarnings("unused")
@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<ActiveFlagSecurityToken> securities;


	public ActiveFlag(UUID id, String activeFlagName, boolean allowAccess)
	{
		this.id = id;
		name = activeFlagName;
		this.allowAccess = allowAccess;
	}

	@Override
	public void configureSecurityEntity(ActiveFlagSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	

	public ActiveFlag setEnterpriseID(Enterprise enterpriseID)
	{
		this.enterpriseID = enterpriseID;
		return this;
	}
	
	@Override
	public ActiveFlag setName(@NotNull @Size(min = 1,
	                                         max = 100) String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	public ActiveFlag setDescription(@NotNull @Size(min = 1,
	                                                max = 100) String description)
	{
		this.description = description;
		return this;
	}
	
	public ActiveFlag setSecurities(List<ActiveFlagSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public ActiveFlag setEnterpriseID(IEnterprise<?, ?> enterpriseID)
	{
		this.enterpriseID = (Enterprise) enterpriseID;
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
	public void configureForClassification(Mutiny.Session session, IWarehouseRelationshipClassificationTable linkTable, IClassification<?, ?> classificationValue, ISystems<?, ?> system)
	{
		ActiveFlagXClassification x = (ActiveFlagXClassification) linkTable;
		x.setActiveFlagID(this);
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
		return Objects.equals(getName(), that.getName());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getName());
	}
	
	@Override
	public @NotNull @Size(min = 1,
	                      max = 100) String getName()
	{
		return name;
	}
	
	@Override
	public @NotNull @Size(min = 1,
	                      max = 100) String getDescription()
	{
		return description;
	}
	
	@Override
	public Enterprise getEnterpriseID()
	{
		return enterpriseID;
	}
	
	public List<ActiveFlagSecurityToken> getSecurities()
	{
		return securities;
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
}
