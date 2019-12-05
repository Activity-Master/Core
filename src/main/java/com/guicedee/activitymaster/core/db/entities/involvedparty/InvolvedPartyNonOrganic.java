package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyNonOrganicQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;

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
@SuppressWarnings("unused")
@Entity
@Table(name = "InvolvedPartyNonOrganic")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyNonOrganic
		extends WarehouseTable<InvolvedPartyNonOrganic, InvolvedPartyNonOrganicQueryBuilder, Long, InvolvedPartyNonOrganicSecurityToken>
{

	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
			name = "InvolvedPartyNonOrganicID")
	private Long id;

	@JoinColumn(name = "InvolvedPartyNonOrganicID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false,
			insertable = false,
			updatable = false)
	@OneToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedParty involvedParty;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyNonOrganicSecurityToken> securities;

	public InvolvedPartyNonOrganic()
	{

	}

	public InvolvedPartyNonOrganic(Long involvedPartyNonOrganicID)
	{
		this.id = involvedPartyNonOrganicID;
	}

	@Override
	protected InvolvedPartyNonOrganicSecurityToken configureDefaultsForNewToken(InvolvedPartyNonOrganicSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public List<InvolvedPartyNonOrganicSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public InvolvedPartyNonOrganic setSecurities(List<InvolvedPartyNonOrganicSecurityToken> securities)
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
		InvolvedPartyNonOrganic that = (InvolvedPartyNonOrganic) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public String toString()
	{
		return "NonOrganicParty - " + getId();
	}

	public Long getId()
	{
		return this.id;
	}

	public InvolvedParty getInvolvedParty()
	{
		return this.involvedParty;
	}

	public InvolvedPartyNonOrganic setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyNonOrganic setInvolvedParty(InvolvedParty involvedParty)
	{
		this.involvedParty = involvedParty;
		return this;
	}
}
