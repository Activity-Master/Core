package com.guicedee.activitymaster.core.db.entities.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IEventType;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

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

@Access(FIELD)
public class EventType
		extends WarehouseSCDNameDescriptionTable<EventType, EventTypeQueryBuilder, Long, EventTypesSecurityToken>
		implements IEventType<EventType>,
				           IActivityMasterEntity<EventType>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventTypeID")
	@JsonValue
	private Long id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 200)
	@Column(nullable = false,
			length = 200,
			name = "EventTypeName")
	@JsonIgnore
	private String name;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 200)
	@Column(nullable = false,
			length = 200,
			name = "EventTypeDesc")
	@JsonIgnore
	private String description;

	@OneToMany(
			mappedBy = "eventTypeID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventXEventType> eventXEventTypeList;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventTypesSecurityToken> securities;

	public EventType()
	{

	}

	public EventType(Long eventTypeID)
	{
		this.id = eventTypeID;
	}

	public EventType(Long eventTypeID, String eventTypName, String eventTypeDesc)
	{
		this.id = eventTypeID;
		this.name = eventTypName;
		this.description = eventTypeDesc;
	}

	@Override
	protected EventTypesSecurityToken configureDefaultsForNewToken(EventTypesSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public List<EventXEventType> getEventXEventTypeList()
	{
		return this.eventXEventTypeList;
	}

	public List<EventTypesSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public EventType setEventXEventTypeList(List<EventXEventType> eventXEventTypeList)
	{
		this.eventXEventTypeList = eventXEventTypeList;
		return this;
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
		return "EventType - " + getName();
	}

	public Long getId()
	{
		return this.id;
	}

	public @NotNull @Size(min = 1,
			max = 200) String getName()
	{
		return this.name;
	}

	public @NotNull @Size(min = 1,
			max = 200) String getDescription()
	{
		return this.description;
	}

	public EventType setId(Long id)
	{
		this.id = id;
		return this;
	}

	public EventType setName(@NotNull @Size(min = 1,
			max = 200) String name)
	{
		this.name = name;
		return this;
	}

	public EventType setDescription(@NotNull @Size(min = 1,
			max = 200) String description)
	{
		this.description = description;
		return this;
	}
}
