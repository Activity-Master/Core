package com.guicedee.activitymaster.core.db.entities.events;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventXGeographyQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IEvent;
import com.guicedee.activitymaster.core.services.dto.IGeography;
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
@Table(schema="Event",name = "EventXGeography")
@XmlRootElement

@Access(FIELD)
public class EventXGeography
		extends WarehouseClassificationRelationshipTable<Event,
				                                                Geography,
				                                                EventXGeography,
				                                                EventXGeographyQueryBuilder,
				                                                java.util.UUID,
				                                                EventXGeographySecurityToken,
				                                                IEvent<?>, IGeography<?>>
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "EventXGeographyID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

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

	public EventXGeography(UUID eventXGeographyID)
	{
		this.id = eventXGeographyID;
	}

	@Override
	protected EventXGeographySecurityToken configureDefaultsForNewToken(EventXGeographySecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public List<EventXGeographySecurityToken> getSecurities()
	{
		return this.securities;
	}

	public Event getEventID()
	{
		return this.eventID;
	}

	public Geography getGeographyID()
	{
		return this.geographyID;
	}

	public EventXGeography setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public EventXGeography setSecurities(List<EventXGeographySecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public EventXGeography setEventID(Event eventID)
	{
		this.eventID = eventID;
		return this;
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
	public IEvent<?> getPrimary()
	{
		return getEventID();
	}

	@Override
	public IGeography<?> getSecondary()
	{
		return getGeographyID();
	}
}
