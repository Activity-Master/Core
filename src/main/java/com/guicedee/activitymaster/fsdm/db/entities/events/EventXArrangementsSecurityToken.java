package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXArrangementSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Event", name = "EventXArrangementsSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class EventXArrangementsSecurityToken
		extends WarehouseSecurityTable<EventXArrangementsSecurityToken, EventXArrangementSecurityTokenQueryBuilder, java.lang.String>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXArrangementsSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "EventXArrangementsID",
	            referencedColumnName = "EventXArrangementsID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private EventXArrangement base;
	
	public EventXArrangementsSecurityToken()
	{
	
	}
	
	public EventXArrangementsSecurityToken(java.lang.String eventXArrangementsSecurityTokenID)
	{
		this.id = eventXArrangementsSecurityTokenID;
	}
	
	public String toString()
	{
		return "EventXArrangementsSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public EventXArrangementsSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public EventXArrangement getBase()
	{
		return this.base;
	}
	
	public EventXArrangementsSecurityToken setBase(EventXArrangement base)
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
		if (!(o instanceof EventXArrangementsSecurityToken))
		{
			return false;
		}
		final EventXArrangementsSecurityToken other = (EventXArrangementsSecurityToken) o;
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
		return other instanceof EventXArrangementsSecurityToken;
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
