package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyNameTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsActiveFlags;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.INameAndDescription;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IInvolvedPartyNameType;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static javax.persistence.AccessType.*;
import static javax.persistence.FetchType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyNameType
		extends WarehouseSCDNameDescriptionTable<InvolvedPartyNameType, InvolvedPartyNameTypeQueryBuilder, Long, InvolvedPartyNameTypeSecurityToken>
		implements INameAndDescription<InvolvedPartyNameType>,
				           IContainsEnterprise<InvolvedPartyNameType>,
				           IActivityMasterEntity<InvolvedPartyNameType>,
				           IContainsActiveFlags<InvolvedPartyNameType>,
				           IInvolvedPartyNameType<InvolvedPartyNameType>

{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyNameTypeID")
	private Long id;
	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 500)
	@Column(nullable = false,
			length = 500,
			name = "InvolvedPartyNameTypeName")
	private String name;
	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 500)
	@Column(nullable = false,
			length = 500,
			name = "InvolvedPartyNameTypeDescr")
	private String description;

	@OneToMany(
			mappedBy = "involvedPartyNameTypeID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyNameType> involvedPartyXInvolvedPartyNameTypeList;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNameTypeSecurityToken> securities;

	public InvolvedPartyNameType()
	{

	}

	public InvolvedPartyNameType(Long involvedPartyNameTypeID)
	{
		this.id = involvedPartyNameTypeID;
	}

	public InvolvedPartyNameType(Long involvedPartyNameTypeID, String involvedPartyName, String involvedPartyNameDescr)
	{
		this.id = involvedPartyNameTypeID;
		this.name = involvedPartyName;
		this.description = involvedPartyNameDescr;
	}

	@Override
	public String toString()
	{
		return "IdentificationNameType - " + getName();
	}

	@Override
	protected InvolvedPartyNameTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyNameTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public List<InvolvedPartyXInvolvedPartyNameType> getInvolvedPartyXInvolvedPartyNameTypeList()
	{
		return this.involvedPartyXInvolvedPartyNameTypeList;
	}

	public List<InvolvedPartyNameTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public InvolvedPartyNameType setInvolvedPartyXInvolvedPartyNameTypeList(List<InvolvedPartyXInvolvedPartyNameType> involvedPartyXInvolvedPartyNameTypeList)
	{
		this.involvedPartyXInvolvedPartyNameTypeList = involvedPartyXInvolvedPartyNameTypeList;
		return this;
	}

	public InvolvedPartyNameType setSecurities(List<InvolvedPartyNameTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof InvolvedPartyNameType))
		{
			return false;
		}
		final InvolvedPartyNameType other = (InvolvedPartyNameType) o;
		if (!other.canEqual((Object) this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
		{
			return false;
		}
		return true;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof InvolvedPartyNameType;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}

	public Long getId()
	{
		return this.id;
	}

	public @NotNull @Size(min = 1,
			max = 500) String getName()
	{
		return this.name;
	}

	public @NotNull @Size(min = 1,
			max = 500) String getDescription()
	{
		return this.description;
	}

	public InvolvedPartyNameType setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyNameType setName(@NotNull @Size(min = 1,
			max = 500) String name)
	{
		this.name = name;
		return this;
	}

	public InvolvedPartyNameType setDescription(@NotNull @Size(min = 1,
			max = 500) String description)
	{
		this.description = description;
		return this;
	}
}
