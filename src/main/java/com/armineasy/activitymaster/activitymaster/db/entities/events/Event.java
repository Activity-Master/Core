package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseTable;
import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.Geography;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.capabilities.*;
import com.armineasy.activitymaster.activitymaster.services.classifications.events.IEventClassification;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "Event")
@XmlRootElement
@Accessors(chain = true)
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class Event
		extends WarehouseTable<Event, EventQueryBuilder, Long, EventSecurityToken>
		implements IContainsClassifications<Event, Classification, EventXClassification, IEventClassification>,
				           IContainsGeographies<Event, Geography, EventXGeography>,
				           IContainsResourceItems<Event, ResourceItem, EventXResourceItem>,
				           IContainsInvolvedParties<Event, InvolvedParty, EventXInvolvedParty>,
				           IContainsAddresses<Event, Address, EventXAddress>,
				           IContainsEventTypes<Event, EventType, EventXEventType>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventID")
	@Getter
	@Setter
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
	protected EventSecurityToken configureDefaultsForNewToken(EventSecurityToken stAdmin, Enterprise enterprise, Systems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public void configureForClassification(EventXClassification classificationLink, Enterprise enterprise)
	{
		classificationLink.setEventID(this);
	}

	@Override
	public void setMyGeographyLinkValue(EventXGeography classificationLink, Geography geography, Enterprise enterprise)
	{
		classificationLink.setEventID(this);
		classificationLink.setGeographyID(geography);
	}

	@Override
	public void setMyResourceItemLinkValue(EventXResourceItem classificationLink, ResourceItem resourceItem, Enterprise enterprise)
	{
		classificationLink.setEventID(this);
		classificationLink.setResourceItemID(resourceItem);
	}

	@Override
	public void setMyInvolvedPartyLinkValue(EventXInvolvedParty classificationLink, InvolvedParty involvedParty, Enterprise enterprise)
	{
		classificationLink.setEventID(this);
		classificationLink.setInvolvedPartyID(involvedParty);
	}

	@Override
	public void setMyAddressLinkValue(EventXAddress classificationLink, Address address, Enterprise enterprise)
	{
		classificationLink.setEventID(this);
		classificationLink.setAddressID(address);
	}

	@Override
	public void setMyEventTypeLinkValue(EventXEventType classificationLink, EventType identificationType, Enterprise enterprise)
	{
		classificationLink.setEventID(this);
		classificationLink.setEventTypeID(identificationType);
	}
}
