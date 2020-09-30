package com.guicedee.activitymaster.core.db.entities.events;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventXEventTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IEvent;
import com.guicedee.activitymaster.core.services.dto.IEventType;
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
@Table(schema = "Event",
       name = "EventXEventType")
@XmlRootElement

@Access(FIELD)
public class EventXEventType
		extends WarehouseClassificationRelationshipTypesTable<Event,
		EventType,
		EventXEventType,
		EventXEventTypeQueryBuilder,
		IEventClassification<?>,
		Long,
		EventXEventTypeSecurityToken,
		IEvent<?>, IEventType<?>>
{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
	        name = "EventXEventTypeID")
	private Long id;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<EventXEventTypeSecurityToken> securities;
	
	@JoinColumn(name = "EventID",
	            referencedColumnName = "EventID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Event eventID;
	@JoinColumn(name = "EventTypeID",
	            referencedColumnName = "EventTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private EventType eventTypeID;
	
	public EventXEventType()
	{
	
	}
	
	public EventXEventType(Long eventXEventTypeID)
	{
		this.id = eventXEventTypeID;
	}
	
	@Override
	protected EventXEventTypeSecurityToken configureDefaultsForNewToken(EventXEventTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	@Override
	public Long getId()
	{
		return this.id;
	}
	
	public List<EventXEventTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public Event getEventID()
	{
		return this.eventID;
	}
	
	public EventType getEventTypeID()
	{
		return this.eventTypeID;
	}
	
	@Override
	public EventXEventType setId(Long id)
	{
		this.id = id;
		return this;
	}
	
	public EventXEventType setSecurities(List<EventXEventTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public EventXEventType setEventID(Event eventID)
	{
		this.eventID = eventID;
		return this;
	}
	
	public EventXEventType setEventTypeID(EventType eventTypeID)
	{
		this.eventTypeID = eventTypeID;
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
		EventXEventType that = (EventXEventType) o;
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
	public IEventType<?> getSecondary()
	{
		return getEventTypeID();
	}
}
