package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXArrangementQueryBuilder;
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
@Table(schema = "Event", name = "EventXArrangement")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class EventXArrangement
		extends WarehouseClassificationRelationshipTable<Event,
		Arrangement,
		EventXArrangement,
		EventXArrangementQueryBuilder,
		UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXArrangementsID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<EventXArrangementsSecurityToken> securities;
	
	@JoinColumn(name = "ArrangementID",
	            referencedColumnName = "ArrangementID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Arrangement arrangementID;
	
	@JoinColumn(name = "EventID",
	            referencedColumnName = "EventID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Event eventID;
	
	public EventXArrangement()
	{
	
	}
	
	public EventXArrangement(UUID eventXArrangementsID)
	{
		this.id = eventXArrangementsID;
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public EventXArrangement setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public List<EventXArrangementsSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public EventXArrangement setSecurities(List<EventXArrangementsSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public Arrangement getArrangementID()
	{
		return this.arrangementID;
	}
	
	public EventXArrangement setArrangementID(Arrangement arrangementID)
	{
		this.arrangementID = arrangementID;
		return this;
	}
	
	public Event getEventID()
	{
		return this.eventID;
	}
	
	public EventXArrangement setEventID(Event eventID)
	{
		this.eventID = eventID;
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
		EventXArrangement that = (EventXArrangement) o;
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
	public Arrangement getSecondary()
	{
		return getArrangementID();
	}
}
