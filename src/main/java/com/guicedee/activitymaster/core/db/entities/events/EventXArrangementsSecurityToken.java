package com.guicedee.activitymaster.core.db.entities.events;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventXArrangementSecurityTokenQueryBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Event",name = "EventXArrangementsSecurityToken")
@XmlRootElement

@Access(FIELD)
public class EventXArrangementsSecurityToken
		extends WarehouseSecurityTable<EventXArrangementsSecurityToken, EventXArrangementSecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventXArrangementsSecurityTokenID")
	private Long id;

	@JoinColumn(name = "EventXArrangementsID",
			referencedColumnName = "EventXArrangementsID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private EventXArrangement base;

	public EventXArrangementsSecurityToken()
	{

	}

	public EventXArrangementsSecurityToken(Long eventXArrangementsSecurityTokenID)
	{
		this.id = eventXArrangementsSecurityTokenID;
	}

	public String toString()
	{
		return "EventXArrangementsSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public EventXArrangement getBase()
	{
		return this.base;
	}

	public EventXArrangementsSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
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
