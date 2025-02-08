package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXClassificationQueryBuilder;
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
@Table(schema = "Event", name = "EventXClassification")
@XmlRootElement

@Access(AccessType.FIELD)
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
		java.lang.String,
		EventXClassificationSecurityToken
		>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXClassificationID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<EventXClassificationSecurityToken> securities;
	
/*
	@JoinColumn(name = "ClassificationID",
	            referencedColumnName = "ClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	@JsonIdentityReference(alwaysAsId = false)
	private Classification classificationID;
	*/
	@JoinColumn(name = "EventID",
	            referencedColumnName = "EventID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Event eventID;
	
	public EventXClassification()
	{
	
	}
	
	public EventXClassification(java.lang.String eventXClassificationID)
	{
		this.id = eventXClassificationID;
	}
	
	@Override
	public void configureSecurityEntity(EventXClassificationSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public EventXClassification setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
/*	@Override
	public Classification getClassificationID()
	{
		return classificationID;
	}
	
	public EventXClassification setClassificationID(Classification classificationID)
	{
		this.classificationID = classificationID;
		return this;
	}
	*/
	public List<EventXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public EventXClassification setSecurities(List<EventXClassificationSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public Event getEventID()
	{
		return this.eventID;
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
