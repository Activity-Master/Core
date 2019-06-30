package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyTypeQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsActiveFlags;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsEnterprise;
import com.armineasy.activitymaster.activitymaster.services.capabilities.INameAndDescription;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IInvolvedPartyType;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

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
@Table
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class InvolvedPartyType
		extends WarehouseSCDNameDescriptionTable<InvolvedPartyType, InvolvedPartyTypeQueryBuilder, Long, InvolvedPartyTypeSecurityToken>
				implements IInvolvedPartyType<InvolvedPartyType>,
						           INameAndDescription<InvolvedPartyType>,
						           IContainsEnterprise<InvolvedPartyType>,
						           IActivityMasterEntity<InvolvedPartyType>,
						           IContainsActiveFlags<InvolvedPartyType>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyTypeID")
	private Long id;
	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 100)
	@Column(nullable = false,
			length = 100,
			name = "InvolvedPartyTypeName")
	private String name;
	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Lob
	@Column(nullable = false,
			name = "InvolvedPartyTypeDesc")
	private String description;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyTypeSecurityToken> securities;

	@OneToMany(
			mappedBy = "involvedPartyTypeID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyType> involvedPartyXInvolvedPartyTypeList;

	public InvolvedPartyType()
	{

	}

	public InvolvedPartyType(Long involvedPartyTypeID)
	{
		this.id = involvedPartyTypeID;
	}

	public InvolvedPartyType(Long involvedPartyTypeID, String involvedPartyTypeName, String involvedPartyTypeDesc)
	{
		this.id = involvedPartyTypeID;
		this.name = involvedPartyTypeName;
		this.description = involvedPartyTypeDesc;
	}

	@Override
	protected InvolvedPartyTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public List<InvolvedPartyTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public List<InvolvedPartyXInvolvedPartyType> getInvolvedPartyXInvolvedPartyTypeList()
	{
		return this.involvedPartyXInvolvedPartyTypeList;
	}

	public InvolvedPartyType setSecurities(List<InvolvedPartyTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public InvolvedPartyType setInvolvedPartyXInvolvedPartyTypeList(List<InvolvedPartyXInvolvedPartyType> involvedPartyXInvolvedPartyTypeList)
	{
		this.involvedPartyXInvolvedPartyTypeList = involvedPartyXInvolvedPartyTypeList;
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
		InvolvedPartyType that = (InvolvedPartyType) o;
		return Objects.equals(getName(), that.getName());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public String toString()
	{
		return "Party Type - " + getName();
	}

	public Long getId()
	{
		return this.id;
	}

	public @NotNull @Size(min = 1,
			max = 100) String getName()
	{
		return this.name;
	}

	public @NotNull String getDescription()
	{
		return this.description;
	}

	public InvolvedPartyType setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyType setName(@NotNull @Size(min = 1,
			max = 100) String name)
	{
		this.name = name;
		return this;
	}

	public InvolvedPartyType setDescription(@NotNull String description)
	{
		this.description = description;
		return this;
	}
}
