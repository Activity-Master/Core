package com.guicedee.activitymaster.core.db.entities.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.address.IAddressClassification;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

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
@Table(schema = "Event",
       name = "Event")
@XmlRootElement

@Access(FIELD)
public class Event
		extends WarehouseTable<Event, EventQueryBuilder, java.util.UUID, EventSecurityToken>
		implements IContainsClassifications<Event, Classification, EventXClassification, IEventClassification<?>, IEvent<?>, IClassification<?>, Event>,
		           IContainsGeographies<Event, Geography, EventXGeography>,
		           IContainsResourceItems<Event, ResourceItem, EventXResourceItem, IClassificationValue<?>, IEvent<?>, IResourceItem<?>, Event>,
		           IContainsInvolvedParties<Event, InvolvedParty, EventXInvolvedParty, IClassificationValue<?>, IEvent<?>, IInvolvedParty<?>, Event>,
		           IContainsAddresses<Event, Address, EventXAddress, IAddressClassification<?>, IEvent<?>, IAddress<?>, Event>,
		           IContainsEventTypes<Event, EventType, EventXEventType, IClassificationValue<?>, IEventClassification<?>, IEvent<?>, IEventType<?>, Event>,
		           IActivityMasterEntity<Event>,
		           IEvent<Event>,
		           IContainsRules<Event, Rules,EventXRules,IClassification<?>,IEvent<?>,IRules<?>>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Basic(optional = false)
	@Column(nullable = false)
	private int dayID;
	@Basic(optional = false)
	@Column(nullable = false)
	private int hourID;
	@Basic(optional = false)
	@Column(nullable = false)
	private int minuteID;
	
	@Id
	
	@Column(nullable = false,
	        name = "EventID")
	@JsonValue@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
	@OneToMany(
			mappedBy = "eventID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventXClassification> classifications;
	
	@OneToMany(
			mappedBy = "eventID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventXInvolvedParty> parties;
	@OneToMany(
			mappedBy = "eventID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventXArrangement> arrangements;
	
	@OneToMany(
			mappedBy = "eventID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventXResourceItem> resources;
	@OneToMany(
			mappedBy = "eventID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventXAddress> addresses;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventSecurityToken> securities;
	@OneToMany(
			mappedBy = "eventID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventXProduct> products;
	@OneToMany(
			mappedBy = "eventID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventXGeography> geographies;
	
	@OneToMany(
			mappedBy = "eventID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventXEventType> eventTypes;
	
	@OneToMany(
			mappedBy = "eventID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventXRules> rules;
	
	public Event()
	{
	
	}
	
	public Event(UUID eventID)
	{
		this.id = eventID;
	}
	
	@Override
	public void configureAddressLinkValue(EventXAddress linkTable, Event primary, Address secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setEventID(primary);
		linkTable.setAddressID(secondary);
	}
	
	@Override
	protected EventSecurityToken configureDefaultsForNewToken(EventSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	@Override
	public void configureForClassification(EventXClassification classificationLink, IEnterprise<?> enterprise)
	{
		classificationLink.setEventID(this);
	}
	
	@Override
	public void setMyGeographyLinkValue(EventXGeography classificationLink, Geography geography, IEnterprise<?> enterprise)
	{
		classificationLink.setEventID(this);
		classificationLink.setGeographyID(geography);
	}
	
	@Override
	public void configureResourceItemLinkValue(EventXResourceItem linkTable, Event primary, ResourceItem secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setEventID(this);
		linkTable.setResourceItemID(secondary);
	}
	
	@Override
	public void configureResourceItemAddable(EventXResourceItem linkTable, Event primary, ResourceItem secondary, IClassificationValue<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setEventID(this);
		linkTable.setResourceItemID(secondary);
	}
	
	@Override
	public void configureEventTypeLinkValue(EventXEventType linkTable, Event primary, EventType secondary, IClassificationValue<?> classificationValue, String value, ISystems<?> system)
	{
		linkTable.setEventID(primary);
		linkTable.setEventTypeID(secondary);
	}
	
	public int getDayID()
	{
		return dayID;
	}
	
	public Event setDayID(int dayID)
	{
		this.dayID = dayID;
		return this;
	}
	
	public int getHourID()
	{
		return hourID;
	}
	
	public Event setHourID(int hourID)
	{
		this.hourID = hourID;
		return this;
	}
	
	public int getMinuteID()
	{
		return minuteID;
	}
	
	public Event setMinuteID(int minuteID)
	{
		this.minuteID = minuteID;
		return this;
	}
	
	public List<EventXClassification> getClassifications()
	{
		return this.classifications;
	}
	
	public List<EventXInvolvedParty> getParties()
	{
		return this.parties;
	}
	
	public List<EventXArrangement> getArrangements()
	{
		return this.arrangements;
	}
	
	public List<EventXResourceItem> getResources()
	{
		return this.resources;
	}
	
	public List<EventXAddress> getAddresses()
	{
		return this.addresses;
	}
	
	public List<EventSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public List<EventXProduct> getProducts()
	{
		return this.products;
	}
	
	public List<EventXGeography> getGeographies()
	{
		return this.geographies;
	}
	
	public List<EventXEventType> getEventTypes()
	{
		return this.eventTypes;
	}
	
	public Event setClassifications(List<EventXClassification> classifications)
	{
		this.classifications = classifications;
		return this;
	}
	
	public Event setParties(List<EventXInvolvedParty> parties)
	{
		this.parties = parties;
		return this;
	}
	
	public Event setArrangements(List<EventXArrangement> arrangements)
	{
		this.arrangements = arrangements;
		return this;
	}
	
	public Event setResources(List<EventXResourceItem> resources)
	{
		this.resources = resources;
		return this;
	}
	
	public Event setAddresses(List<EventXAddress> addresses)
	{
		this.addresses = addresses;
		return this;
	}
	
	public Event setSecurities(List<EventSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public Event setProducts(List<EventXProduct> products)
	{
		this.products = products;
		return this;
	}
	
	public Event setGeographies(List<EventXGeography> geographies)
	{
		this.geographies = geographies;
		return this;
	}
	
	public Event setEventTypes(List<EventXEventType> eventTypes)
	{
		this.eventTypes = eventTypes;
		return this;
	}
	
	@Override
	public String toString()
	{
		return "Event - " + getId();
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
		Event event = (Event) o;
		return Objects.equals(getId(), event.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	@Override
	public Event setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public void setMyInvolvedPartyLinkValue(EventXInvolvedParty classificationLink, Event first, InvolvedParty involvedParty, IEnterprise<?> enterprise)
	{
		classificationLink.setEventID(first);
		classificationLink.setInvolvedPartyID(involvedParty);
	}
	
	@Override
	public void configureAddableRule(EventXRules linkTable, Event primary, Rules secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setEventID(primary);
		linkTable.setRulesID(secondary);
		linkTable.setClassificationID((Classification) classificationValue);
		linkTable.setValue(value);
	}
}
