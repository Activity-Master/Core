package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXInvolvedPartyQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Event", name = "EventXInvolvedParty")
@XmlRootElement

@Access(AccessType.FIELD)
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
		java.lang.String,
		EventXInvolvedPartySecurityToken
		>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXInvolvedPartyID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
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
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<EventXInvolvedPartySecurityToken> securities;
	
	public EventXInvolvedParty()
	{
	
	}
	
	public EventXInvolvedParty(java.lang.String eventXInvolvedPartyID)
	{
		this.id = eventXInvolvedPartyID;
	}
	
	@Override
	public void configureSecurityEntity(EventXInvolvedPartySecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public EventXInvolvedParty setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public Event getEventID()
	{
		return this.eventID;
	}
	
	public EventXInvolvedParty setEventID(Event eventID)
	{
		this.eventID = eventID;
		return this;
	}
	
	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}
	
	public EventXInvolvedParty setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}
	
	public List<EventXInvolvedPartySecurityToken> getSecurities()
	{
		return this.securities;
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
