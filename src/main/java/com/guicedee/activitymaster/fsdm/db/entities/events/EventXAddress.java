/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.Address;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXAddressQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Event", name = "EventXAddress")
@XmlRootElement

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class EventXAddress
		extends WarehouseClassificationRelationshipTable<Event,
		Address,
		EventXAddress,
		EventXAddressQueryBuilder,
		java.lang.String,
		EventXAddressSecurityToken
		>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXAddressID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
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
	
	public EventXAddress(java.lang.String eventXAddressID)
	{
		this.id = eventXAddressID;
	}
	
	public EventXAddress setId(java.lang.String id)
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
	public Event getPrimary()
	{
		return getEventID();
	}
	
	@Override
	public Address getSecondary()
	{
		return getAddressID();
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public List<EventXAddressSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public Address getAddressID()
	{
		return addressID;
	}
	
	public Event getEventID()
	{
		return eventID;
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
		return Objects.hashCode(getId());
	}
}
