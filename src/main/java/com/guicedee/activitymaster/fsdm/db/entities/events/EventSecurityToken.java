package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

import java.io.Serial;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Getter
@Entity
@Table(schema = "Event", name = "EventSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class EventSecurityToken
		extends IWarehouseSecurityTable<EventSecurityToken, EventSecurityTokenQueryBuilder, String>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventsSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "EventsID",
	            referencedColumnName = "EventID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Event base;
	
	public EventSecurityToken()
	{
	
	}
	
	public EventSecurityToken(java.lang.String eventsSecurityTokenID)
	{
		this.id = eventsSecurityTokenID;
	}
	
	public String toString()
	{
		return "EventSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public EventSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public EventSecurityToken setBase(Event base)
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
		if (!(o instanceof EventSecurityToken))
		{
			return false;
		}
		final EventSecurityToken other = (EventSecurityToken) o;
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
		return other instanceof EventSecurityToken;
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
