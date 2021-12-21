package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXRulesQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.rules.Rules;
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
@Table(schema = "Event", name = "EventXRules")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class EventXRules
		extends WarehouseClassificationRelationshipTable<Event,
		Rules,
		EventXRules,
		EventXRulesQueryBuilder,
		UUID>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXRulesID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<EventXRulesSecurityToken> securities;
	@JoinColumn(name = "EventID",
	            referencedColumnName = "EventID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Event eventID;
	@JoinColumn(name = "RulesID",
	            referencedColumnName = "RulesID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Rules rulesID;
	
	public EventXRules()
	{
	
	}
	
	public EventXRules(UUID eventXRulesID)
	{
		this.id = eventXRulesID;
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public EventXRules setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public List<EventXRulesSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public EventXRules setSecurities(List<EventXRulesSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public Event getEventID()
	{
		return this.eventID;
	}
	
	public EventXRules setEventID(Event eventID)
	{
		this.eventID = eventID;
		return this;
	}
	
	public Rules getRulesID()
	{
		return this.rulesID;
	}
	
	public EventXRules setRulesID(Rules resourceItemID)
	{
		this.rulesID = resourceItemID;
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
		EventXRules that = (EventXRules) o;
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
	public Rules getSecondary()
	{
		return getRulesID();
	}
}
