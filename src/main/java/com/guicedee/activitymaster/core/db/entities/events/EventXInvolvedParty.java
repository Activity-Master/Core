package com.guicedee.activitymaster.core.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventXInvolvedPartyQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class EventXInvolvedParty
		extends WarehouseClassificationRelationshipTable<Event,
				                                                InvolvedParty,
				                                                EventXInvolvedParty,
				                                                EventXInvolvedPartyQueryBuilder,
				                                                java.util.UUID>
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
	public Event getPrimary()
	{
		return getEventID();
	}

	@Override
	public InvolvedParty getSecondary()
	{
		return getInvolvedPartyID();
	}
}
