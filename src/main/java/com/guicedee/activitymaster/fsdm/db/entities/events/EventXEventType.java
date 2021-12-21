package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXEventTypeQueryBuilder;
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
@Entity
@Table(schema = "Event",
       name = "EventXEventType")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class EventXEventType
		extends WarehouseClassificationRelationshipTypesTable<Event,
		EventType,
		EventXEventType,
		EventXEventTypeQueryBuilder,
		UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXEventTypeID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
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
	
	public EventXEventType(UUID eventXEventTypeID)
	{
		this.id = eventXEventTypeID;
	}
	
	@Override
	public UUID getId()
	{
		return this.id;
	}
	
	@Override
	public EventXEventType setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public List<EventXEventTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public EventXEventType setSecurities(List<EventXEventTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public Event getEventID()
	{
		return this.eventID;
	}
	
	public EventXEventType setEventID(Event eventID)
	{
		this.eventID = eventID;
		return this;
	}
	
	public EventType getEventTypeID()
	{
		return this.eventTypeID;
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
	public Event getPrimary()
	{
		return getEventID();
	}
	
	@Override
	public EventType getSecondary()
	{
		return getEventTypeID();
	}
}
