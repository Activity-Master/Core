package com.guicedee.activitymaster.core.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.client.services.builders.warehouse.address.IAddress;
import com.guicedee.activitymaster.client.services.builders.warehouse.arrangements.IArrangement;
import com.guicedee.activitymaster.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.client.services.builders.warehouse.events.IEventType;
import com.guicedee.activitymaster.client.services.builders.warehouse.geography.IGeography;
import com.guicedee.activitymaster.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.client.services.builders.warehouse.rules.IRules;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class Event
		extends WarehouseTable<Event, EventQueryBuilder, java.util.UUID>
		implements IEvent<Event,EventQueryBuilder>
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
		private List<EventXClassification> classifications;
	
	@OneToMany(
			mappedBy = "eventID",
			fetch = FetchType.LAZY)
		private List<EventXInvolvedParty> parties;
	@OneToMany(
			mappedBy = "eventID",
			fetch = FetchType.LAZY)
		private List<EventXArrangement> arrangements;
	
	@OneToMany(
			mappedBy = "eventID",
			fetch = FetchType.LAZY)
		private List<EventXResourceItem> resources;
	@OneToMany(
			mappedBy = "eventID",
			fetch = FetchType.LAZY)
		private List<EventXAddress> addresses;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
		private List<EventSecurityToken> securities;
	@OneToMany(
			mappedBy = "eventID",
			fetch = FetchType.LAZY)
		private List<EventXProduct> products;
	@OneToMany(
			mappedBy = "eventID",
			fetch = FetchType.LAZY)
		private List<EventXGeography> geographies;
	
	@OneToMany(
			mappedBy = "eventID",
			fetch = FetchType.LAZY)
		private List<EventXEventType> eventTypes;
	
	@OneToMany(
			mappedBy = "eventID",
			fetch = FetchType.LAZY)
		private List<EventXRules> rules;
	
	public Event()
	{
	
	}
	
	public Event(UUID eventID)
	{
		this.id = eventID;
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
	public void configureAddressLinkValue(IWarehouseRelationshipTable linkTable, Event primary, IAddress<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?,?> system)
	{
		EventXAddress e = (EventXAddress) linkTable;
	}
	
	@Override
	public void configureArrangementAddable(IWarehouseRelationshipTable linkTable, Event primary, IArrangement<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?,?> system)
	{
	
	}
	
	@Override
	public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable, ISystems<?,?> system)
	{
	
	}
	
	@Override
	public void configureEventTypeLinkValue(IWarehouseRelationshipTable linkTable, Event primary, IEventType<?, ?> secondary, IClassification<?, ?> classificationValue, String value, IEnterprise<?,?> enterprise)
	{
	
	}
	
	@Override
	public void configureGeographyAddable(IWarehouseRelationshipTable linkTable, Event primary, IGeography<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?,?> system)
	{
	
	}
	
	@Override
	public void configureInvolvedPartyAddable(IWarehouseRelationshipTable linkTable, Event primary, IInvolvedParty<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?,?> system)
	{
	
	}
	
	@Override
	public void configureProductAddable(IWarehouseRelationshipTable linkTable, Event primary, IProduct<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?,?> system)
	{
	
	}
	
	@Override
	public void configureResourceItemAddable(IWarehouseRelationshipTable linkTable, Event primary, IResourceItem<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?,?> system)
	{
	
	}
	
	@Override
	public void configureRulesAddable(IWarehouseRelationshipTable linkTable, Event primary, IRules<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?,?> system)
	{
	
	}
}
