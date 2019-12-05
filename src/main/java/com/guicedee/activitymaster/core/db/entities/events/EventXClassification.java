package com.guicedee.activitymaster.core.db.entities.events;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IEvent;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "EventXClassification")
@XmlRootElement

@Access(FIELD)
public class EventXClassification
		extends WarehouseClassificationRelationshipTable<Event,
				                                                Classification,
				                                                EventXClassification,
				                                                EventXClassificationQueryBuilder,
				                                                Long,
				                                                EventXClassificationSecurityToken,
				                                                IEvent<?>, IClassification<?>>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventXClassificationID")
	private Long id;

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

	public EventXClassification(Long eventXClassificationID)
	{
		this.id = eventXClassificationID;
	}

	@Override
	protected EventXClassificationSecurityToken configureDefaultsForNewToken(EventXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
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

	public EventXClassification setId(Long id)
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
	public IEvent<?> getPrimary()
	{
		return getEventID();
	}

	@Override
	public IClassification<?> getSecondary()
	{
		return getClassificationID();
	}
}
