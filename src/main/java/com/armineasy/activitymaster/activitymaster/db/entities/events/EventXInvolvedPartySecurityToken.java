package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventXInvolvedPartySecurityTokenQueryBuilder;
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
@Table(name = "EventXInvolvedPartySecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class EventXInvolvedPartySecurityToken
		extends WarehouseSecurityTable<EventXInvolvedPartySecurityToken, EventXInvolvedPartySecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventXInvolvedPartySecurityTokenID")
	private Long id;

	@JoinColumn(name = "EventXInvolvedPartyID",
			referencedColumnName = "EventXInvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private EventXInvolvedParty base;

	public EventXInvolvedPartySecurityToken()
	{

	}

	public EventXInvolvedPartySecurityToken(Long eventXInvolvedPartySecurityTokenID)
	{
		this.id = eventXInvolvedPartySecurityTokenID;
	}

	public String toString()
	{
		return "EventXInvolvedPartySecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public EventXInvolvedParty getBase()
	{
		return this.base;
	}

	public EventXInvolvedPartySecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public EventXInvolvedPartySecurityToken setBase(EventXInvolvedParty base)
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
		if (!(o instanceof EventXInvolvedPartySecurityToken))
		{
			return false;
		}
		final EventXInvolvedPartySecurityToken other = (EventXInvolvedPartySecurityToken) o;
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
		return other instanceof EventXInvolvedPartySecurityToken;
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
