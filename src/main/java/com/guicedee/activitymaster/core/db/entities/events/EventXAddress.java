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

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

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
				                                                Long,
				                                                EventXAddressSecurityToken,
				                                                IEvent<?>, IAddress<?>>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventXAddressID")
	private Long id;

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

	public EventXAddress(Long eventXAddressID)
	{
		this.id = eventXAddressID;
	}

	@Override
	protected EventXAddressSecurityToken configureDefaultsForNewToken(EventXAddressSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
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

	public EventXAddress setId(Long id)
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
