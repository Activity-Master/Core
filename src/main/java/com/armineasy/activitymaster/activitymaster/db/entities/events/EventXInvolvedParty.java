package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventXInvolvedPartyQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
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
@Table(name = "EventXInvolvedParty")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class EventXInvolvedParty
		extends WarehouseClassificationRelationshipTable<Event, InvolvedParty, EventXInvolvedParty, EventXInvolvedPartyQueryBuilder, Long, EventXInvolvedPartySecurityToken>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventXInvolvedPartyID")
	private Long id;

	@JoinColumn(name = "EventID",
			referencedColumnName = "EventID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Event eventID;
	@JoinColumn(name = "InvolvedPartyID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedParty involvedPartyID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<EventXInvolvedPartySecurityToken> securities;

	public EventXInvolvedParty()
	{

	}

	public EventXInvolvedParty(Long eventXInvolvedPartyID)
	{
		this.id = eventXInvolvedPartyID;
	}

	@Override
	protected EventXInvolvedPartySecurityToken configureDefaultsForNewToken(EventXInvolvedPartySecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "EventXInvolvedParty(id=" + this.getId() + ", eventID=" + this.getEventID() + ", involvedPartyID=" + this.getInvolvedPartyID() + ", securities=" +
		       this.getSecurities() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public Event getEventID()
	{
		return this.eventID;
	}

	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}

	public List<EventXInvolvedPartySecurityToken> getSecurities()
	{
		return this.securities;
	}

	public EventXInvolvedParty setId(Long id)
	{
		this.id = id;
		return this;
	}

	public EventXInvolvedParty setEventID(Event eventID)
	{
		this.eventID = eventID;
		return this;
	}

	public EventXInvolvedParty setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}

	public EventXInvolvedParty setSecurities(List<EventXInvolvedPartySecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof EventXInvolvedParty))
		{
			return false;
		}
		final EventXInvolvedParty other = (EventXInvolvedParty) o;
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
		return other instanceof EventXInvolvedParty;
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
