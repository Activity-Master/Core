package com.guicedee.activitymaster.core.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventXClassificationQueryBuilder;
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
@Table(schema="Event",name = "EventXClassification")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class EventXClassification
		extends WarehouseClassificationRelationshipTable<Event,
				                                                Classification,
				                                                EventXClassification,
				                                                EventXClassificationQueryBuilder,
				                                                java.util.UUID>
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "EventXClassificationID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<EventXClassificationSecurityToken> securities;

	@JoinColumn(name = "EventID",
			referencedColumnName = "EventID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Event eventID;

	public EventXClassification()
	{

	}

	public EventXClassification(UUID eventXClassificationID)
	{
		this.id = eventXClassificationID;
	}
	
	public java.util.UUID getId()
	{
		return this.id;
	}

	public List<EventXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public Event getEventID()
	{
		return this.eventID;
	}

	public EventXClassification setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public EventXClassification setSecurities(List<EventXClassificationSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public EventXClassification setEventID(Event eventID)
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
		EventXClassification that = (EventXClassification) o;
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
	public Classification getSecondary()
	{
		return getClassificationID();
	}
}
