package com.guicedee.activitymaster.core.db.entities.events;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventXInvolvedPartyQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IEvent;
import com.guicedee.activitymaster.core.services.dto.IInvolvedParty;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Event",name = "EventXInvolvedParty")
@XmlRootElement

@Access(FIELD)
public class EventXInvolvedParty
		extends WarehouseClassificationRelationshipTable<Event,
				                                                InvolvedParty,
				                                                EventXInvolvedParty,
				                                                EventXInvolvedPartyQueryBuilder,
				                                                java.util.UUID,
				                                                EventXInvolvedPartySecurityToken,
				                                                IEvent<?>, IInvolvedParty<?>>
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
			name = "EventXInvolvedPartyID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@JoinColumn(name = "EventID",
			referencedColumnName = "EventID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Event eventID;
	@JoinColumn(name = "InvolvedPartyID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedParty involvedPartyID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<EventXInvolvedPartySecurityToken> securities;

	public EventXInvolvedParty()
	{

	}

	public EventXInvolvedParty(UUID eventXInvolvedPartyID)
	{
		this.id = eventXInvolvedPartyID;
	}

	@Override
	protected EventXInvolvedPartySecurityToken configureDefaultsForNewToken(EventXInvolvedPartySecurityToken stAdmin,  ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public Event getEventID()
	{
		return this.eventID;
	}

	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}

	public List<EventXInvolvedPartySecurityToken> getSecurities()
	{
		return this.securities;
	}

	public EventXInvolvedParty setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public EventXInvolvedParty setEventID(Event eventID)
	{
		this.eventID = eventID;
		return this;
	}

	public EventXInvolvedParty setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}

	public EventXInvolvedParty setSecurities(List<EventXInvolvedPartySecurityToken> securities)
	{
		this.securities = securities;
		return this;
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
		EventXInvolvedParty that = (EventXInvolvedParty) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IEvent<?> getPrimary()
	{
		return getEventID();
	}

	@Override
	public IInvolvedParty<?> getSecondary()
	{
		return getInvolvedPartyID();
	}
}
