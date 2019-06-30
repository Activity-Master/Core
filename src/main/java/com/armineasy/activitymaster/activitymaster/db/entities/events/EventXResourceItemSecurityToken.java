/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventXResourceItemSecurityTokenQueryBuilder;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "EventXResourceItemSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class EventXResourceItemSecurityToken
		extends WarehouseSecurityTable<EventXResourceItemSecurityToken, EventXResourceItemSecurityTokenQueryBuilder, Long>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventXResourceItemSecurityTokenID")
	private Long id;

	@JoinColumn(name = "EventXResourceItemID",
			referencedColumnName = "EventXResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private EventXResourceItem base;

	public EventXResourceItemSecurityToken()
	{

	}

	public EventXResourceItemSecurityToken(Long eventXResourceItemSecurityTokenID)
	{
		this.id = eventXResourceItemSecurityTokenID;
	}

	public String toString()
	{
		return "EventXResourceItemSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public EventXResourceItem getBase()
	{
		return this.base;
	}

	public EventXResourceItemSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public EventXResourceItemSecurityToken setBase(EventXResourceItem base)
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
		if (!(o instanceof EventXResourceItemSecurityToken))
		{
			return false;
		}
		final EventXResourceItemSecurityToken other = (EventXResourceItemSecurityToken) o;
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
		return other instanceof EventXResourceItemSecurityToken;
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
