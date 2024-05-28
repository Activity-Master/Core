/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventTypeSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.Objects;


/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Event", name = "EventTypesSecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
public class EventTypesSecurityToken
		extends WarehouseSecurityTable<EventTypesSecurityToken, EventTypeSecurityTokenQueryBuilder, String>
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
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public EventType getBase()
	{
		return base;
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
		EventTypesSecurityToken that = (EventTypesSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
