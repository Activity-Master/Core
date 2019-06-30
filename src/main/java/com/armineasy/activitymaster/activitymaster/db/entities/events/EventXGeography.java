package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventXGeographyQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.Geography;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "EventXGeography")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class EventXGeography
		extends WarehouseClassificationRelationshipTable<Event, Geography, EventXGeography, EventXGeographyQueryBuilder, Long, EventXGeographySecurityToken>
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
	protected EventXGeographySecurityToken configureDefaultsForNewToken(EventXGeographySecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "EventXGeography(id=" + this.getId() + ", securities=" + this.getSecurities() + ", eventID=" + this.getEventID() + ", geographyID=" + this.getGeographyID() + ")";
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof EventXGeography))
		{
			return false;
		}
		final EventXGeography other = (EventXGeography) o;
		if (!other.canEqual((Object) this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
		{
			return false;
		}
		return true;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof EventXGeography;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}
