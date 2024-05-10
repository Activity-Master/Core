/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventTypeSecurityTokenQueryBuilder;
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
@Table(schema = "Event", name = "EventTypesSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class EventTypesSecurityToken
		extends IWarehouseSecurityTable<EventTypesSecurityToken, EventTypeSecurityTokenQueryBuilder, String>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventTypesSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "EventTypesID",
	            referencedColumnName = "EventTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private EventType base;
	
	public EventTypesSecurityToken()
	{
	
	}
	
	public EventTypesSecurityToken(java.lang.String eventTypesSecurityTokenID)
	{
		this.id = eventTypesSecurityTokenID;
	}
	
	public String toString()
	{
		return "EventTypesSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public EventTypesSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public EventTypesSecurityToken setBase(EventType base)
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
		if (!(o instanceof EventTypesSecurityToken))
		{
			return false;
		}
		final EventTypesSecurityToken other = (EventTypesSecurityToken) o;
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
		return other instanceof EventTypesSecurityToken;
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
