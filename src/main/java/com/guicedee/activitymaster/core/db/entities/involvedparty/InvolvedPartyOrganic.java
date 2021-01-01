package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyOrganicQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(schema="Party",name = "InvolvedPartyOrganic")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyOrganic
		extends WarehouseTable<InvolvedPartyOrganic, InvolvedPartyOrganicQueryBuilder, java.util.UUID, InvolvedPartyOrganicSecurityToken>

{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
			name = "InvolvedPartyOrganicID")
	@JsonValue@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@JoinColumn(name = "InvolvedPartyOrganicID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false,
			insertable = false,
			updatable = false)
	@OneToOne(optional = false,
			fetch = FetchType.LAZY)
	@JsonIgnore
	private InvolvedParty involvedParty;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<InvolvedPartyOrganicSecurityToken> securities;

	public InvolvedPartyOrganic()
	{

	}

	public InvolvedPartyOrganic(UUID involvedPartyOrganicID)
	{
		this.id = involvedPartyOrganicID;
	}

	@Override
	protected InvolvedPartyOrganicSecurityToken configureDefaultsForNewToken(InvolvedPartyOrganicSecurityToken stAdmin,  ISystems<?> enterprise, ISystems<?> activityMasterSystem)
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

	public java.util.UUID getId()
	{
		return this.id;
	}

	public InvolvedParty getInvolvedParty()
	{
		return this.involvedParty;
	}

	public InvolvedPartyOrganic setId(java.util.UUID id)
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
