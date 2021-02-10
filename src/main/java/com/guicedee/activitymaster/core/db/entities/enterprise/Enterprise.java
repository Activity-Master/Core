package com.guicedee.activitymaster.core.db.entities.enterprise;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.capabilities.IContainsNameAndDescription;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseClassification;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.IEnterpriseService;
import com.guicedee.guicedinjection.GuiceContext;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "Enterprise")
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
public class Enterprise
		extends WarehouseNameDescriptionTable<Enterprise, EnterpriseQueryBuilder, java.util.UUID, EnterpriseSecurityToken>
		implements IContainsClassifications<Enterprise, Classification, EnterpriseXClassification, IEnterpriseClassification<?>, IEnterprise<?>, IClassification<?>, Enterprise>,
		           IActivityMasterEntity<Enterprise>,
		           IContainsNameAndDescription<Enterprise>,
		           IEnterprise<Enterprise>,
		           IEnterpriseName
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	
	@Column(nullable = false,
	        name = "EnterpriseID")
	@JsonValue
	@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
	@Basic(optional = false,
	       fetch = FetchType.EAGER)
	@NotNull
	@Column(nullable = false,
	        name = "EnterpriseName")
	private String name;
	@Basic(optional = false,
	       fetch = FetchType.EAGER)
	@NotNull
	@Column(nullable = false,
	        name = "EnterpriseDesc")
	private String description;
	
	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	private List<EnterpriseSecurityToken> securities;
	
	public Enterprise()
	{
	
	}
	
	public Enterprise(java.util.UUID id)
	{
		this.id = id;
	}
	
	public Enterprise(java.util.UUID id, String enterpriseName, String enterpriseDesc)
	{
		this.id = id;
		name = enterpriseName;
		description = enterpriseDesc;
	}
	
	@Override
	public String toString()
	{
		return "Enterprise - " + getName();
	}
	
	@Override
	public void configureForClassification(EnterpriseXClassification classificationLink, ISystems<?> system)
	{
	
	}
	
	@Override
	public Enterprise remove()
	{
		setEffectiveToDate(LocalDateTime.now());
		updateNow();
		return this;
	}
	
	@Override
	public Enterprise archive()
	{
		setEffectiveToDate(LocalDateTime.now());
		updateNow();
		return this;
	}
	
	public List<EnterpriseSecurityToken> getSecurities()
	{
		return securities;
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
		Enterprise that = (Enterprise) o;
		return Objects.equals(getName(), that.getName());
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
	public @NotNull String getName()
	{
		return name;
	}
	
	@Override
	public @NotNull String getDescription()
	{
		return description;
	}
	
	@Override
	public Enterprise setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public Enterprise setName(@NotNull String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	public Enterprise setDescription(@NotNull String description)
	{
		this.description = description;
		return this;
	}
	
	@Override
	public IEnterpriseName<?> getIEnterprise()
	{
		return GuiceContext.get(IEnterpriseService.class)
		                   .getIEnterprise(this);
	}
	
	@Override
	public IEnterprise<?> getEnterprise()
	{
		return this;
	}
	
	@Override
	public void setEnterprise(IEnterprise enterprise)
	{
	
	}
	
	@Override
	public String name()
	{
		return getName();
	}
	
	@Override
	public String classificationDescription()
	{
		return getDescription();
	}
	
}
