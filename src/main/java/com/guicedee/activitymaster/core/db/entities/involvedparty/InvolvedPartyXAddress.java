/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyXAddressQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IAddress;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IInvolvedParty;
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
@Entity
@Table(schema = "Party",
		name = "InvolvedPartyXAddress")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyXAddress
		extends WarehouseClassificationRelationshipTable<InvolvedParty,
				                                                Address,
				                                                InvolvedPartyXAddress,
				                                                InvolvedPartyXAddressQueryBuilder,
				                                                java.util.UUID,
				                                                InvolvedPartyXAddressSecurityToken,
				                                                IInvolvedParty<?>, IAddress<?>>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
			name = "InvolvedPartyXAddressID")
	@JsonValue@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@JoinColumn(name = "AddressID",
			referencedColumnName = "AddressID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Address addressID;

	@JoinColumn(name = "InvolvedPartyID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedParty involvedPartyID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXAddressSecurityToken> securities;

	public InvolvedPartyXAddress()
	{

	}

	public InvolvedPartyXAddress(UUID involvedPartyXAddressID)
	{
		this.id = involvedPartyXAddressID;
	}

	@Override
	protected InvolvedPartyXAddressSecurityToken configureDefaultsForNewToken(InvolvedPartyXAddressSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public Address getAddressID()
	{
		return this.addressID;
	}

	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}

	public List<InvolvedPartyXAddressSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public InvolvedPartyXAddress setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyXAddress setAddressID(Address addressID)
	{
		this.addressID = addressID;
		return this;
	}

	public InvolvedPartyXAddress setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}

	public InvolvedPartyXAddress setSecurities(List<InvolvedPartyXAddressSecurityToken> securities)
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
		InvolvedPartyXAddress that = (InvolvedPartyXAddress) o;
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
	public IAddress<?> getSecondary()
	{
		return getAddressID();
	}
}
