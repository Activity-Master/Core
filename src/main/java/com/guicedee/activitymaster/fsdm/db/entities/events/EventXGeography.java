package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXGeographyQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.geography.Geography;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Event", name = "EventXGeography")
@XmlRootElement

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class EventXGeography
		extends WarehouseClassificationRelationshipTable<Event,
		Geography,
		EventXGeography,
		EventXGeographyQueryBuilder,
		java.lang.String>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXGeographyID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<EventXGeographySecurityToken> securities;
	
	@JoinColumn(name = "EventID",
	            referencedColumnName = "EventID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Event eventID;
	@JoinColumn(name = "GeographyID",
	            referencedColumnName = "GeographyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Geography geographyID;
	
	public EventXGeography()
	{
	
	}
	
	public EventXGeography(java.lang.String eventXGeographyID)
	{
		this.id = eventXGeographyID;
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public EventXGeography setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public List<EventXGeographySecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public EventXGeography setSecurities(List<EventXGeographySecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public Event getEventID()
	{
		return this.eventID;
	}
	
	public EventXGeography setEventID(Event eventID)
	{
		this.eventID = eventID;
		return this;
	}
	
	public Geography getGeographyID()
	{
		return this.geographyID;
	}
	
	public EventXGeography setGeographyID(Geography geographyID)
	{
		this.geographyID = geographyID;
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
		EventXGeography that = (EventXGeography) o;
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
	public Geography getSecondary()
	{
		return getGeographyID();
	}
}
