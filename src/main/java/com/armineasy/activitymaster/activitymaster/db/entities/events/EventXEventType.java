package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventXEventTypeQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "EventXEventType")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class EventXEventType
		extends WarehouseClassificationRelationshipTable<Event, EventType, EventXEventType, EventXEventTypeQueryBuilder, Long, EventXEventTypeSecurityToken>
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
	protected EventXEventTypeSecurityToken configureDefaultsForNewToken(EventXEventTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "EventXEventType(id=" + this.getId() + ", securities=" + this.getSecurities() + ", eventID=" + this.getEventID() + ", eventTypeID=" + this.getEventTypeID() + ")";
	}

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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof EventXEventType))
		{
			return false;
		}
		final EventXEventType other = (EventXEventType) o;
		if (!other.canEqual((Object) this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
		{
			return false;
		}
		return true;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof EventXEventType;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}
