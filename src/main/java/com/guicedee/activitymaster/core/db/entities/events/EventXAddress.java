/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.events;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventXAddressQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IAddress;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IEvent;
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
@Table(schema="Event",name = "EventXAddress")
@XmlRootElement

@Access(FIELD)
public class EventXAddress
		extends WarehouseClassificationRelationshipTable<Event,
				                                                Address,
				                                                EventXAddress,
				                                                EventXAddressQueryBuilder,
				                                                java.util.UUID,
				                                                EventXAddressSecurityToken,
				                                                IEvent<?>, IAddress<?>>
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
			name = "EventXAddressID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<EventXAddressSecurityToken> securities;

	@JoinColumn(name = "AddressID",
			referencedColumnName = "AddressID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Address addressID;

	@JoinColumn(name = "EventID",
			referencedColumnName = "EventID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Event eventID;

	public EventXAddress()
	{

	}

	public EventXAddress(UUID eventXAddressID)
	{
		this.id = eventXAddressID;
	}

	@Override
	protected EventXAddressSecurityToken configureDefaultsForNewToken(EventXAddressSecurityToken stAdmin, ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public List<EventXAddressSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public Address getAddressID()
	{
		return this.addressID;
	}

	public Event getEventID()
	{
		return this.eventID;
	}

	public EventXAddress setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public EventXAddress setSecurities(List<EventXAddressSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public EventXAddress setAddressID(Address addressID)
	{
		this.addressID = addressID;
		return this;
	}

	public EventXAddress setEventID(Event eventID)
	{
		this.eventID = eventID;
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
		EventXAddress that = (EventXAddress) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IEvent<?> getPrimary()
	{
		return getEventID();
	}

	@Override
	public IAddress<?> getSecondary()
	{
		return getAddressID();
	}
}
