package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyOrganicTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsActiveFlags;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.IContainsNameAndDescription;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IInvolvedPartyOrganicType;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;
import static jakarta.persistence.FetchType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(schema = "Party",
		name = "InvolvedPartyOrganicType")
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
public class InvolvedPartyOrganicType
		extends WarehouseSCDNameDescriptionTable<InvolvedPartyOrganicType, InvolvedPartyOrganicTypeQueryBuilder, java.util.UUID, InvolvedPartyOrganicTypeSecurityToken>
		implements IInvolvedPartyOrganicType<InvolvedPartyOrganicType>,
		           IContainsNameAndDescription<InvolvedPartyOrganicType>,
				           IContainsEnterprise<InvolvedPartyOrganicType>,
				           IActivityMasterEntity<InvolvedPartyOrganicType>,
				   IContainsActiveFlags<InvolvedPartyOrganicType>
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "InvolvedPartyOrganicTypeID")
	@JsonValue@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	@Basic(optional = false,
			fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 200)
	@Column(nullable = false,
			length = 200,
			name = "InvolvedPartyTypeName")
		private String name;
	@Basic(optional = false,
			fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 500)
	@Column(nullable = false,
			length = 500,
			name = "InvolvedPartyTypeDesc")
		private String description;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyOrganicTypeSecurityToken> securities;

	public InvolvedPartyOrganicType()
	{

	}

	public InvolvedPartyOrganicType(UUID involvedPartyOrganicTypeID)
	{
		id = involvedPartyOrganicTypeID;
	}

	@Override
	public String toString()
	{
		return "OrganicPartyType - " + getName();
	}

	@Override
	protected InvolvedPartyOrganicTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyOrganicTypeSecurityToken stAdmin,  ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public List<InvolvedPartyOrganicTypeSecurityToken> getSecurities()
	{
		return securities;
	}

	public InvolvedPartyOrganicType setSecurities(List<InvolvedPartyOrganicTypeSecurityToken> securities)
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
		InvolvedPartyOrganicType that = (InvolvedPartyOrganicType) o;
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
	public @NotNull @Size(min = 1,
			max = 200) String getName()
	{
		return name;
	}

	@Override
	public String getDescription()
	{
		return description;
	}

	@Override
	public InvolvedPartyOrganicType setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	@Override
	public InvolvedPartyOrganicType setName(@NotNull @Size(min = 1,
			max = 200) String name)
	{
		this.name = name;
		return this;
	}

	@Override
	public InvolvedPartyOrganicType setDescription(@NotNull @Size(min = 1,
			max = 500) String description)
	{
		this.description = description;
		return this;
	}
}
