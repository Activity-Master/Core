package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseNameAndDescriptionTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEventType;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventTypeQueryBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@SuppressWarnings("unused")
@Entity
@Table(schema = "Event",
       name = "EventType")
@XmlRootElement

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class EventType
		extends WarehouseSCDTable<EventType, EventTypeQueryBuilder, String,EventTypesSecurityToken>
		implements IEventType<EventType, EventTypeQueryBuilder>,
		           IWarehouseNameAndDescriptionTable<EventType,EventTypeQueryBuilder,String>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventTypeID")
	@JsonValue
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
	      max = 200)
	@Column(nullable = false,
	        length = 200,
	        name = "EventTypeName")
	private String name;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
	      max = 200)
	@Column(nullable = false,
	        length = 200,
	        name = "EventTypeDesc")
	private String description;
	
	@OneToMany(
			mappedBy = "eventTypeID",
			fetch = FetchType.LAZY)
	
	private List<EventXEventType> eventXEventTypeList;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<EventTypesSecurityToken> securities;
	
	public EventType()
	{
	
	}
	
	public EventType(java.lang.String eventTypeID)
	{
		this.id = eventTypeID;
	}
	
	public EventType(java.lang.String eventTypeID, String eventTypName, String eventTypeDesc)
	{
		this.id = eventTypeID;
		this.name = eventTypName;
		this.description = eventTypeDesc;
	}
	
	@Override
	public void configureSecurityEntity(EventTypesSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	public List<EventXEventType> getEventXEventTypeList()
	{
		return this.eventXEventTypeList;
	}
	
	public EventType setEventXEventTypeList(List<EventXEventType> eventXEventTypeList)
	{
		this.eventXEventTypeList = eventXEventTypeList;
		return this;
	}
	
	public List<EventTypesSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public EventType setSecurities(List<EventTypesSecurityToken> securities)
	{
		this.securities = securities;
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
		EventType eventType = (EventType) o;
		return Objects.equals(getName(), eventType.getName());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public EventType setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public @NotNull @Size(min = 1,
	                      max = 200) String getName()
	{
		return this.name;
	}
	
	public EventType setName(@NotNull @Size(min = 1,
	                                        max = 200) String name)
	{
		this.name = name;
		return this;
	}
	
	public @NotNull @Size(min = 1,
	                      max = 200) String getDescription()
	{
		return this.description;
	}
	
	public EventType setDescription(@NotNull @Size(min = 1,
	                                               max = 200) String description)
	{
		this.description = description;
		return this;
	}
}
