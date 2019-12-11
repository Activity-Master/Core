package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsActiveFlags;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.INameAndDescription;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IInvolvedPartyIdentificationType;
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
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(schema="Party",name = "InvolvedPartyIdentificationType")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyIdentificationType
		extends WarehouseSCDNameDescriptionTable<InvolvedPartyIdentificationType, InvolvedPartyIdentificationTypeQueryBuilder, Long, InvolvedPartyIdentificationTypeSecurityToken>
		implements IInvolvedPartyIdentificationType<InvolvedPartyIdentificationType>,
				           INameAndDescription<InvolvedPartyIdentificationType>,
				           IContainsEnterprise<InvolvedPartyIdentificationType>,
				           IActivityMasterEntity<InvolvedPartyIdentificationType>,
				           IContainsActiveFlags<InvolvedPartyIdentificationType>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyIdentificationTypeID")
	private Long id;
	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 150)
	@Column(nullable = false,
			length = 150,
			name = "InvolvedPartyIdentificationName")
	private String name;
	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 500)
	@Column(nullable = false,
			length = 500,
			name = "InvolvedPartyIdentificationDesc")
	private String description;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyIdentificationTypeSecurityToken> securities;

	@OneToMany(
			mappedBy = "involvedPartyIdentificationTypeID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyIdentificationType> involvedPartyXInvolvedPartyIdentificationTypeList;

	public InvolvedPartyIdentificationType()
	{

	}

	public InvolvedPartyIdentificationType(Long involvedPartyIdentificationTypeID)
	{
		this.id = involvedPartyIdentificationTypeID;
	}

	public InvolvedPartyIdentificationType(Long involvedPartyIdentificationTypeID, String involvedPartyIdentificationName, String involvedPartyIdentificationDesc)
	{
		this.id = involvedPartyIdentificationTypeID;
		this.name = involvedPartyIdentificationName;
		this.description = involvedPartyIdentificationDesc;
	}

	@Override
	protected InvolvedPartyIdentificationTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyIdentificationTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public String toString()
	{
		return "IdentificationType - " + getName();
	}

	public List<InvolvedPartyIdentificationTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public List<InvolvedPartyXInvolvedPartyIdentificationType> getInvolvedPartyXInvolvedPartyIdentificationTypeList()
	{
		return this.involvedPartyXInvolvedPartyIdentificationTypeList;
	}

	public InvolvedPartyIdentificationType setSecurities(List<InvolvedPartyIdentificationTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public InvolvedPartyIdentificationType setInvolvedPartyXInvolvedPartyIdentificationTypeList(List<InvolvedPartyXInvolvedPartyIdentificationType> involvedPartyXInvolvedPartyIdentificationTypeList)
	{
		this.involvedPartyXInvolvedPartyIdentificationTypeList = involvedPartyXInvolvedPartyIdentificationTypeList;
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
		InvolvedPartyIdentificationType that = (InvolvedPartyIdentificationType) o;
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

	public   String getName()
	{
		return this.name;
	}

	public    String getDescription()
	{
		return this.description;
	}

	public InvolvedPartyIdentificationType setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyIdentificationType setName(  String name)
	{
		this.name = name;
		return this;
	}

	public InvolvedPartyIdentificationType setDescription(@NotNull @Size(min = 1,
			max = 500) String description)
	{
		this.description = description;
		return this;
	}
}
