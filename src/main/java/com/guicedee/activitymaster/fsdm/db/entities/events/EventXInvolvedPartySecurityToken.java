package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXInvolvedPartySecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Event", name = "EventXInvolvedPartySecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class EventXInvolvedPartySecurityToken
		extends WarehouseSecurityTable<EventXInvolvedPartySecurityToken, EventXInvolvedPartySecurityTokenQueryBuilder, UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXInvolvedPartySecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
	@JoinColumn(name = "EventXInvolvedPartyID",
	            referencedColumnName = "EventXInvolvedPartyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private EventXInvolvedParty base;
	
	public EventXInvolvedPartySecurityToken()
	{
	
	}
	
	public EventXInvolvedPartySecurityToken(UUID eventXInvolvedPartySecurityTokenID)
	{
		this.id = eventXInvolvedPartySecurityTokenID;
	}
	
	public String toString()
	{
		return "EventXInvolvedPartySecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public EventXInvolvedPartySecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public EventXInvolvedParty getBase()
	{
		return this.base;
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
