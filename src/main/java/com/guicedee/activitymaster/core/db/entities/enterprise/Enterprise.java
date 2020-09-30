package com.guicedee.activitymaster.core.db.entities.enterprise;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.capabilities.INameAndDescription;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseClassification;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.system.IEnterpriseService;
import com.guicedee.guicedinjection.GuiceContext;
import io.github.classgraph.ClassInfo;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

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
public class Enterprise
		extends WarehouseNameDescriptionTable<Enterprise, EnterpriseQueryBuilder, Long, EnterpriseSecurityToken>
		implements IContainsClassifications<Enterprise, Classification, EnterpriseXClassification, IEnterpriseClassification<?>, IEnterprise<?>, IClassification<?>, Enterprise>,
				           IActivityMasterEntity<Enterprise>,
				           INameAndDescription<Enterprise>,
				           IEnterprise<Enterprise>
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EnterpriseID")
	@JsonValue
	private Long id;

	@Basic(optional = false,
			fetch = FetchType.EAGER)
	@NotNull
	@Column(nullable = false,
			name = "EnterpriseName")
	@JsonIgnore
	private String name;
	@Basic(optional = false,
			fetch = FetchType.EAGER)
	@NotNull
	@Column(nullable = false,
			name = "EnterpriseDesc")
	@JsonIgnore
	private String description;

	@OneToMany(
			mappedBy = "enterpriseID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EnterpriseSecurityToken> securities;

	public Enterprise()
	{

	}

	public Enterprise(Long id)
	{
		this.id = id;
	}

	public Enterprise(Long id, String enterpriseName, String enterpriseDesc)
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
	public void configureForClassification(EnterpriseXClassification classificationLink, IEnterprise<?> enterprise)
	{

	}

	@Override
	@SuppressWarnings("unchecked")
	public Enterprise remove()
	{
		setEffectiveToDate(LocalDateTime.now());
		updateNow();
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
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
	public Long getId()
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
	public Enterprise setId(Long id)
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
}
