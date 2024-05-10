/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXRulesSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;


/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Event", name = "EventXRulesSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class EventXRulesSecurityToken
		extends IWarehouseSecurityTable<EventXRulesSecurityToken, EventXRulesSecurityTokenQueryBuilder, String>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXRulesSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "EventXRulesID",
	            referencedColumnName = "EventXRulesID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private EventXRules base;
	
	public EventXRulesSecurityToken()
	{
	
	}
	
	public EventXRulesSecurityToken(java.lang.String eventXRulesSecurityTokenID)
	{
		this.id = eventXRulesSecurityTokenID;
	}
	
	public String toString()
	{
		return "EventXRulesSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public EventXRulesSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public EventXRules getBase()
	{
		return this.base;
	}
	
	public EventXRulesSecurityToken setBase(EventXRules base)
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
		if (!(o instanceof EventXRulesSecurityToken))
		{
			return false;
		}
		final EventXRulesSecurityToken other = (EventXRulesSecurityToken) o;
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
		return other instanceof EventXRulesSecurityToken;
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
