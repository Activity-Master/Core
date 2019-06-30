/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventXAddressQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "EventXAddress")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class EventXAddress
		extends WarehouseClassificationRelationshipTable<Event, Address, EventXAddress, EventXAddressQueryBuilder, Long, EventXAddressSecurityToken>
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
	protected EventXAddressSecurityToken configureDefaultsForNewToken(EventXAddressSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "EventXAddress(id=" + this.getId() + ", securities=" + this.getSecurities() + ", addressID=" + this.getAddressID() + ", eventID=" + this.getEventID() + ")";
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof EventXAddress))
		{
			return false;
		}
		final EventXAddress other = (EventXAddress) o;
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
		return other instanceof EventXAddress;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}
