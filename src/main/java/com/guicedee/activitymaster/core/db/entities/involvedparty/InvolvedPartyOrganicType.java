package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyOrganicTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsActiveFlags;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.INameAndDescription;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IInvolvedPartyOrganicType;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;
import static javax.persistence.FetchType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "InvolvedPartyOrganicType")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyOrganicType
		extends WarehouseSCDNameDescriptionTable<InvolvedPartyOrganicType, InvolvedPartyOrganicTypeQueryBuilder, Long, InvolvedPartyOrganicTypeSecurityToken>
		implements IInvolvedPartyOrganicType<InvolvedPartyOrganicType>,
				           INameAndDescription<InvolvedPartyOrganicType>,
				           IContainsEnterprise<InvolvedPartyOrganicType>,
				           IActivityMasterEntity<InvolvedPartyOrganicType>,
				           IContainsActiveFlags<InvolvedPartyOrganicType>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyOrganicTypeID")
	private Long id;
	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 200)
	@Column(nullable = false,
			length = 200,
			name = "InvolvedPartyTypeName")
	private String name;
	@Basic(optional = false,fetch = EAGER)
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

	public InvolvedPartyOrganicType(Long involvedPartyOrganicTypeID)
	{
		this.id = involvedPartyOrganicTypeID;
	}

	@Override
	public String toString()
	{
		return "OrganicPartyType - " + getName();
	}

	@Override
	protected InvolvedPartyOrganicTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyOrganicTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public List<InvolvedPartyOrganicTypeSecurityToken> getSecurities()
	{
		return this.securities;
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

	public Long getId()
	{
		return this.id;
	}

	public @NotNull @Size(min = 1,
			max = 200) String getName()
	{
		return this.name;
	}

	public @NotNull @Size(min = 1,
			max = 500) String getDescription()
	{
		return this.description;
	}

	public InvolvedPartyOrganicType setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyOrganicType setName(@NotNull @Size(min = 1,
			max = 200) String name)
	{
		this.name = name;
		return this;
	}

	public InvolvedPartyOrganicType setDescription(@NotNull @Size(min = 1,
			max = 500) String description)
	{
		this.description = description;
		return this;
	}
}
