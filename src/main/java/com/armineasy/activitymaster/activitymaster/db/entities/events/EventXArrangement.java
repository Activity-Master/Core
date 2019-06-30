package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.Arrangement;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventXArrangementQueryBuilder;
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
@Table(name = "EventXArrangement")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class EventXArrangement
		extends WarehouseClassificationRelationshipTable<Event, Arrangement, EventXArrangement, EventXArrangementQueryBuilder, Long, EventXArrangementsSecurityToken>
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
	protected EventXArrangementsSecurityToken configureDefaultsForNewToken(EventXArrangementsSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "EventXArrangement(id=" + this.getId() + ", securities=" + this.getSecurities() + ", arrangementID=" + this.getArrangementID() + ", eventID=" + this.getEventID() +
		       ")";
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof EventXArrangement))
		{
			return false;
		}
		final EventXArrangement other = (EventXArrangement) o;
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
		return other instanceof EventXArrangement;
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
