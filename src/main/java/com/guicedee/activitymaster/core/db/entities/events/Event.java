package com.guicedee.activitymaster.core.db.entities.events;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IEventTypeValue;

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
@Table(name = "Event")
@XmlRootElement

@Access(FIELD)
public class Event
		extends WarehouseTable<Event, EventQueryBuilder, Long, EventSecurityToken>
		implements IContainsClassifications<Event, Classification, EventXClassification, IEventClassification<?>, IEvent<?>, IClassification<?>, Event>,
				           IContainsGeographies<Event, Geography, EventXGeography>,
				           IContainsResourceItems<Event, ResourceItem, EventXResourceItem, IResourceItemClassification<?>,IEvent<?>, IResourceItem<?>, Event>,
				           IContainsInvolvedParties<Event, InvolvedParty, EventXInvolvedParty>,
				           IContainsAddresses<Event, Address, EventXAddress>,
				           IContainsEventTypes<Event, EventType, EventXEventType, IEventTypeValue<?>,Event>,
				           IActivityMasterEntity<Event>,
				           IEvent<Event>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventID")
	private Long id;

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

	public Event()
	{

	}

	public Event(Long eventID)
	{
		this.id = eventID;
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
	public void setMyInvolvedPartyLinkValue(EventXInvolvedParty classificationLink, InvolvedParty involvedParty, IEnterprise<?> enterprise)
	{
		classificationLink.setEventID(this);
		classificationLink.setInvolvedPartyID(involvedParty);
	}

	@Override
	public void setMyAddressLinkValue(EventXAddress classificationLink, Address address, IEnterprise<?> enterprise)
	{
		classificationLink.setEventID(this);
		classificationLink.setAddressID(address);
	}

	@Override
	public void configureEventTypeLinkValue(EventXEventType linkTable, Event primary, EventType secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setEventID(primary);
		linkTable.setEventTypeID(secondary);
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

	public Long getId()
	{
		return this.id;
	}

	public Event setId(Long id)
	{
		this.id = id;
		return this;
	}


}
