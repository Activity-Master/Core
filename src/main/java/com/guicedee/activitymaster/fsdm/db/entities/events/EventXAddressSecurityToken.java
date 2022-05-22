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
import java.util.UUID;

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
		extends WarehouseSecurityTable<EventXAddressSecurityToken, EventXAddressSecurityTokenQueryBuilder, UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXAddressSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
	@JoinColumn(name = "EventXAddressID",
	            referencedColumnName = "EventXAddressID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private EventXAddress base;
	
	public EventXAddressSecurityToken()
	{
	
	}
	
	public EventXAddressSecurityToken(UUID eventXAddressSecurityTokenID)
	{
		this.id = eventXAddressSecurityTokenID;
	}
	
	public String toString()
	{
		return "EventXAddressSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public EventXAddressSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public EventXAddress getBase()
	{
		return this.base;
	}
	
	public EventXAddressSecurityToken setBase(EventXAddress base)
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
		if (!(o instanceof EventXAddressSecurityToken))
		{
			return false;
		}
		final EventXAddressSecurityToken other = (EventXAddressSecurityToken) o;
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
		return other instanceof EventXAddressSecurityToken;
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
