package com.guicedee.activitymaster.core.db.entities.events;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventXGeographyQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IEvent;
import com.guicedee.activitymaster.core.services.dto.IGeography;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

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
				                                                Long,
				                                                EventXGeographySecurityToken,
				                                                IEvent<?>, IGeography<?>>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventXGeographyID")
	private Long id;

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

	public EventXGeography(Long eventXGeographyID)
	{
		this.id = eventXGeographyID;
	}

	@Override
	protected EventXGeographySecurityToken configureDefaultsForNewToken(EventXGeographySecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
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

	public EventXGeography setId(Long id)
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
