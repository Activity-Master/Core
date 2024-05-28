/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXAddressSecurityTokenQueryBuilder;
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
@Table(schema = "Event", name = "EventXAddressSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class EventXAddressSecurityToken
		extends WarehouseSecurityTable<EventXAddressSecurityToken, EventXAddressSecurityTokenQueryBuilder, String>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXAddressSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "EventXAddressID",
	            referencedColumnName = "EventXAddressID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private EventXAddress base;
	
	public EventXAddressSecurityToken()
	{
	
	}
	
	public EventXAddressSecurityToken(java.lang.String eventXAddressSecurityTokenID)
	{
		this.id = eventXAddressSecurityTokenID;
	}
	
	public String toString()
	{
		return "EventXAddressSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public EventXAddressSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public EventXAddressSecurityToken setBase(EventXAddress base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public EventXAddress getBase()
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
		EventXAddressSecurityToken that = (EventXAddressSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
