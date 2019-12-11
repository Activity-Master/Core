package com.guicedee.activitymaster.core.db.entities.events;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventXArrangementQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IArrangement;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IEvent;
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
@Table(schema="Event",name = "EventXArrangement")
@XmlRootElement

@Access(FIELD)
public class EventXArrangement
		extends WarehouseClassificationRelationshipTable<Event,
				                                                Arrangement,
				                                                EventXArrangement,
				                                                EventXArrangementQueryBuilder,
				                                                Long,
				                                                EventXArrangementsSecurityToken,
				                                                IEvent<?>, IArrangement<?>>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventXArrangementsID")
	private Long id;

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

	public EventXArrangement(Long eventXArrangementsID)
	{
		this.id = eventXArrangementsID;
	}

	@Override
	protected EventXArrangementsSecurityToken configureDefaultsForNewToken(EventXArrangementsSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
	{
		return this.id;
	}

	public List<EventXArrangementsSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public Arrangement getArrangementID()
	{
		return this.arrangementID;
	}

	public Event getEventID()
	{
		return this.eventID;
	}

	public EventXArrangement setId(Long id)
	{
		this.id = id;
		return this;
	}

	public EventXArrangement setSecurities(List<EventXArrangementsSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public EventXArrangement setArrangementID(Arrangement arrangementID)
	{
		this.arrangementID = arrangementID;
		return this;
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
	public IEvent<?> getPrimary()
	{
		return getEventID();
	}

	@Override
	public IArrangement<?> getSecondary()
	{
		return getArrangementID();
	}
}
