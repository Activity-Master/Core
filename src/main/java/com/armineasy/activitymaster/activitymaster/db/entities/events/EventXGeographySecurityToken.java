/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventXGeographySecurityTokenQueryBuilder;
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
@Table(name = "EventXGeographySecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class EventXGeographySecurityToken
		extends WarehouseSecurityTable<EventXGeographySecurityToken, EventXGeographySecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventXGeographySecurityTokenID")
	private Long id;

	@JoinColumn(name = "EventXGeographyID",
			referencedColumnName = "EventXGeographyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private EventXGeography base;

	public EventXGeographySecurityToken()
	{

	}

	public EventXGeographySecurityToken(Long eventXGeographySecurityTokenID)
	{
		this.id = eventXGeographySecurityTokenID;
	}

	public String toString()
	{
		return "EventXGeographySecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public EventXGeography getBase()
	{
		return this.base;
	}

	public EventXGeographySecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public EventXGeographySecurityToken setBase(EventXGeography base)
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
		if (!(o instanceof EventXGeographySecurityToken))
		{
			return false;
		}
		final EventXGeographySecurityToken other = (EventXGeographySecurityToken) o;
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
		return other instanceof EventXGeographySecurityToken;
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
