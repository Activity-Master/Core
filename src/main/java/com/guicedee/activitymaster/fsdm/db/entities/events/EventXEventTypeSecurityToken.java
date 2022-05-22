package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXEventTypeSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.UUID;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Event", name = "EventXEventTypeSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class EventXEventTypeSecurityToken
		extends WarehouseSecurityTable<EventXEventTypeSecurityToken, EventXEventTypeSecurityTokenQueryBuilder, UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXEventTypeSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
	@JoinColumn(name = "EventXEventTypeID",
	            referencedColumnName = "EventXEventTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private EventXEventType base;
	
	public EventXEventTypeSecurityToken()
	{
	
	}
	
	public EventXEventTypeSecurityToken(UUID eventXEventTypeSecurityTokenID)
	{
		this.id = eventXEventTypeSecurityTokenID;
	}
	
	public String toString()
	{
		return "EventXEventTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public EventXEventTypeSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public EventXEventType getBase()
	{
		return this.base;
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
		if (!other.canEqual(this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		return this$id == null ? other$id == null : this$id.equals(other$id);
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
