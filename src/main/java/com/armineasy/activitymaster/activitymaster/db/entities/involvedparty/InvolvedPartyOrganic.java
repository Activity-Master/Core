package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseTable;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyOrganicQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

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
@Table(name = "InvolvedPartyOrganic")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class InvolvedPartyOrganic
		extends WarehouseTable<InvolvedPartyOrganic, InvolvedPartyOrganicQueryBuilder, Long, InvolvedPartyOrganicSecurityToken>

{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
			name = "InvolvedPartyOrganicID")
	private Long id;

	@JoinColumn(name = "InvolvedPartyOrganicID",
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
	private List<InvolvedPartyOrganicSecurityToken> securities;

	public InvolvedPartyOrganic()
	{

	}

	public InvolvedPartyOrganic(Long involvedPartyOrganicID)
	{
		this.id = involvedPartyOrganicID;
	}

	@Override
	protected InvolvedPartyOrganicSecurityToken configureDefaultsForNewToken(InvolvedPartyOrganicSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public List<InvolvedPartyOrganicSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public InvolvedPartyOrganic setSecurities(List<InvolvedPartyOrganicSecurityToken> securities)
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
		InvolvedPartyOrganic that = (InvolvedPartyOrganic) o;
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
		return "OrganicParty - " + getId();
	}

	public Long getId()
	{
		return this.id;
	}

	public InvolvedParty getInvolvedParty()
	{
		return this.involvedParty;
	}

	public InvolvedPartyOrganic setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyOrganic setInvolvedParty(InvolvedParty involvedParty)
	{
		this.involvedParty = involvedParty;
		return this;
	}
}
