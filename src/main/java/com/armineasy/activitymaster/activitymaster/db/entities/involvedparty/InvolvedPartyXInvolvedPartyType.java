package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyTypeQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IInvolvedParty;
import com.armineasy.activitymaster.activitymaster.services.dto.IInvolvedPartyType;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "InvolvedPartyXInvolvedPartyType")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyXInvolvedPartyType
		extends WarehouseRelationshipTable<InvolvedParty, InvolvedPartyType,
				                                  InvolvedPartyXInvolvedPartyType,
				                                  InvolvedPartyXInvolvedPartyTypeQueryBuilder, Long,
				                                  InvolvedPartyXInvolvedPartyTypeSecurityToken,
				                                  IInvolvedParty<?>, IInvolvedPartyType<?>>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyXInvolvedPartyTypeID")
	private Long id;

	@JoinColumn(name = "InvolvedPartyID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedParty involvedPartyID;
	@JoinColumn(name = "InvolvedPartyTypeID",
			referencedColumnName = "InvolvedPartyTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedPartyType involvedPartyTypeID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyTypeSecurityToken> securities;

	public InvolvedPartyXInvolvedPartyType()
	{

	}

	public InvolvedPartyXInvolvedPartyType(Long involvedPartyXInvolvedPartyTypeID)
	{
		this.id = involvedPartyXInvolvedPartyTypeID;
	}

	@Override
	protected InvolvedPartyXInvolvedPartyTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyXInvolvedPartyTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
	{
		return this.id;
	}

	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}

	public InvolvedPartyType getInvolvedPartyTypeID()
	{
		return this.involvedPartyTypeID;
	}

	public List<InvolvedPartyXInvolvedPartyTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public InvolvedPartyXInvolvedPartyType setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyXInvolvedPartyType setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}

	public InvolvedPartyXInvolvedPartyType setInvolvedPartyTypeID(InvolvedPartyType involvedPartyTypeID)
	{
		this.involvedPartyTypeID = involvedPartyTypeID;
		return this;
	}

	public InvolvedPartyXInvolvedPartyType setSecurities(List<InvolvedPartyXInvolvedPartyTypeSecurityToken> securities)
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
		InvolvedPartyXInvolvedPartyType that = (InvolvedPartyXInvolvedPartyType) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IInvolvedParty<?> getPrimary()
	{
		return getInvolvedPartyID();
	}

	@Override
	public IInvolvedPartyType<?> getSecondary()
	{
		return getInvolvedPartyTypeID();
	}
}
