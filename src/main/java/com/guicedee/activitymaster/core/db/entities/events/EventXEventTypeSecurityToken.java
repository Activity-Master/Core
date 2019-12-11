package com.guicedee.activitymaster.core.db.entities.events;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventXEventTypeSecurityTokenQueryBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Event",name = "EventXEventTypeSecurityToken")
@XmlRootElement

@Access(FIELD)
public class EventXEventTypeSecurityToken
		extends WarehouseSecurityTable<EventXEventTypeSecurityToken, EventXEventTypeSecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventXEventTypeSecurityTokenID")
	private Long id;

	@JoinColumn(name = "EventXEventTypeID",
			referencedColumnName = "EventXEventTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private EventXEventType base;

	public EventXEventTypeSecurityToken()
	{

	}

	public EventXEventTypeSecurityToken(Long eventXEventTypeSecurityTokenID)
	{
		this.id = eventXEventTypeSecurityTokenID;
	}

	public String toString()
	{
		return "EventXEventTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public EventXEventType getBase()
	{
		return this.base;
	}

	public EventXEventTypeSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public EventXEventTypeSecurityToken setBase(EventXEventType base)
	{
		this.base = base;
		return this;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof EventXEventTypeSecurityToken))
		{
			return false;
		}
		final EventXEventTypeSecurityToken other = (EventXEventTypeSecurityToken) o;
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
		return other instanceof EventXEventTypeSecurityToken;
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
