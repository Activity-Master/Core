package com.guicedee.activitymaster.core.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventXEventQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.io.Serializable;
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
       name = "EventXEvent")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class EventXEvent
		extends WarehouseClassificationRelationshipTable<Event,
		Event,
		EventXEvent,
		EventXEventQueryBuilder,
		UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXEventID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<EventXEventSecurityToken> securities;
	
	@JoinColumn(name = "ChildEventID",
	            referencedColumnName = "EventID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Event childEventID;
	@JoinColumn(name = "ParentEventID",
	            referencedColumnName = "EventID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Event parentEventID;
	
	public EventXEvent()
	{
	
	}
	
	public EventXEvent(UUID productXEventID)
	{
		id = productXEventID;
	}
	
	@Override
	public UUID getId()
	{
		return id;
	}
	
	public List<EventXEventSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public Event getChildEventID()
	{
		return childEventID;
	}
	
	public Event getParentEventID()
	{
		return parentEventID;
	}
	
	@Override
	public EventXEvent setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public EventXEvent setSecurities(List<EventXEventSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public EventXEvent setChildEventID(Event childEventID)
	{
		this.childEventID = childEventID;
		return this;
	}
	
	public EventXEvent setParentEventID(Event parentEventID)
	{
		this.parentEventID = parentEventID;
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
		EventXEvent that = (EventXEvent) o;
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
		return getParentEventID();
	}
	
	@Override
	public Event getSecondary()
	{
		return getChildEventID();
	}
}
