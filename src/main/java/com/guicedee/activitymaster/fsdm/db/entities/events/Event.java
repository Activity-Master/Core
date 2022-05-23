package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangement;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEventType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.geography.IGeography;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRules;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.Address;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.geography.Geography;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.rules.Rules;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings({"unused", "rawtypes"})
@Entity
@Table(schema = "Event",
       name = "Event")
@XmlRootElement

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class Event
		extends WarehouseTable<Event, EventQueryBuilder, java.lang.String>
		implements IEvent<Event, EventQueryBuilder>
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
	@JsonValue
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
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
	
	public Event(java.lang.String eventID)
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
	
	public Event setClassifications(List<EventXClassification> classifications)
	{
		this.classifications = classifications;
		return this;
	}
	
	public List<EventXInvolvedParty> getParties()
	{
		return this.parties;
	}
	
	public Event setParties(List<EventXInvolvedParty> parties)
	{
		this.parties = parties;
		return this;
	}
	
	public List<EventXArrangement> getArrangements()
	{
		return this.arrangements;
	}
	
	public Event setArrangements(List<EventXArrangement> arrangements)
	{
		this.arrangements = arrangements;
		return this;
	}
	
	public List<EventXResourceItem> getResources()
	{
		return this.resources;
	}
	
	public Event setResources(List<EventXResourceItem> resources)
	{
		this.resources = resources;
		return this;
	}
	
	public List<EventXAddress> getAddresses()
	{
		return this.addresses;
	}
	
	public Event setAddresses(List<EventXAddress> addresses)
	{
		this.addresses = addresses;
		return this;
	}
	
	public List<EventSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public Event setSecurities(List<EventSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public List<EventXProduct> getProducts()
	{
		return this.products;
	}
	
	public Event setProducts(List<EventXProduct> products)
	{
		this.products = products;
		return this;
	}
	
	public List<EventXGeography> getGeographies()
	{
		return this.geographies;
	}
	
	public Event setGeographies(List<EventXGeography> geographies)
	{
		this.geographies = geographies;
		return this;
	}
	
	public List<EventXEventType> getEventTypes()
	{
		return this.eventTypes;
	}
	
	public Event setEventTypes(List<EventXEventType> eventTypes)
	{
		this.eventTypes = eventTypes;
		return this;
	}
	
	@Override
	public String toString()
	{
		return getId() + "";
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
	public java.lang.String getId()
	{
		return this.id;
	}
	
	@Override
	public Event setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public void configureAddressLinkValue(IWarehouseRelationshipTable linkTable, Event primary, IAddress<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
	{
		EventXAddress e = (EventXAddress) linkTable;
		e.setEventID(primary);
		e.setAddressID((Address) secondary);
		e.setClassificationID(classificationValue);
		e.setValue(value);
	}
	
	@Override
	public void configureArrangementAddable(IWarehouseRelationshipTable linkTable, Event primary, IArrangement<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
	{
		EventXArrangement e = (EventXArrangement) linkTable;
		e.setEventID(primary);
		e.setArrangementID((Arrangement) secondary);
		e.setClassificationID(classificationValue);
		e.setValue(value);
	}
	
	@Override
	public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable, IClassification<?, ?> classificationValue, ISystems<?, ?> system)
	{
		EventXClassification e = (EventXClassification) linkTable;
		e.setEventID(this);
		e.setClassificationID(classificationValue);
	}
	
	@Override
	public void configureEventTypeLinkValue(IWarehouseRelationshipTable linkTable, Event primary, IEventType<?, ?> secondary, IClassification<?, ?> classificationValue, String value, IEnterprise<?, ?> enterprise)
	{
		EventXEventType e = (EventXEventType) linkTable;
		e.setEventID(primary);
		e.setEventTypeID((EventType) secondary);
		e.setClassificationID(classificationValue);
		e.setValue(value);
	}
	
	@Override
	public void configureGeographyAddable(IWarehouseRelationshipTable linkTable, Event primary, IGeography<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
	{
		EventXGeography e = (EventXGeography) linkTable;
		e.setEventID(primary);
		e.setGeographyID((Geography) secondary);
		e.setClassificationID(classificationValue);
		e.setValue(value);
		
	}
	
	@Override
	public void configureInvolvedPartyAddable(IWarehouseRelationshipTable linkTable, Event primary, IInvolvedParty<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
	{
		EventXInvolvedParty e = (EventXInvolvedParty) linkTable;
		e.setEventID(primary);
		e.setInvolvedPartyID((InvolvedParty) secondary);
		e.setClassificationID(classificationValue);
		e.setValue(value);
	}
	
	@Override
	public void configureProductAddable(IWarehouseRelationshipTable linkTable, Event primary, IProduct<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
	{
		EventXProduct product = (EventXProduct) linkTable;
		product.setEventID(primary);
		product.setProductID((Product) secondary);
		product.setClassificationID(classificationValue);
		product.setValue(value);
		
	}
	
	@Override
	public void configureResourceItemAddable(IWarehouseRelationshipTable linkTable, Event primary, IResourceItem<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
	{
		EventXResourceItem e = (EventXResourceItem) linkTable;
		e.setEventID(primary);
		e.setResourceItemID((ResourceItem) secondary);
		e.setClassificationID(classificationValue);
		e.setValue(value);
		
	}
	
	@Override
	public void configureRulesAddable(IWarehouseRelationshipTable linkTable, Event primary, IRules<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
	{
		EventXRules e = (EventXRules) linkTable;
		e.setEventID(primary);
		e.setRulesID((Rules) secondary);
		e.setClassificationID(classificationValue);
		e.setValue(value);
		
	}
	
	@Override
	public void configureNewHierarchyItem(IWarehouseRelationshipClassificationTable newLink, Event parent, Event child, String value)
	{
		EventXEvent e = (EventXEvent) newLink;
		e.setParentEventID(parent);
		e.setChildEventID(child);
		e.setValue(value);
	}
}
