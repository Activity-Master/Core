package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXEventQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Event",
       name = "EventXEvent")
@XmlRootElement
@Access(AccessType.FIELD)
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
		java.lang.String,
		EventXEventSecurityToken>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXEventID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
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
	
	public EventXEvent(java.lang.String productXEventID)
	{
		id = productXEventID;
	}
	
	@Override
	public void configureSecurityEntity(EventXEventSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	@Override
	public java.lang.String getId()
	{
		return id;
	}
	
	@Override
	public EventXEvent setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public List<EventXEventSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public EventXEvent setSecurities(List<EventXEventSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public Event getChildEventID()
	{
		return childEventID;
	}
	
	public EventXEvent setChildEventID(Event childEventID)
	{
		this.childEventID = childEventID;
		return this;
	}
	
	public Event getParentEventID()
	{
		return parentEventID;
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
