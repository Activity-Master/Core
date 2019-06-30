package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventXClassificationQueryBuilder;
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
@Table(name = "EventXClassification")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class EventXClassification
		extends WarehouseClassificationRelationshipTable<Event, Classification, EventXClassification, EventXClassificationQueryBuilder, Long, EventXClassificationSecurityToken>
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
	protected EventXClassificationSecurityToken configureDefaultsForNewToken(EventXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "EventXClassification(id=" + this.getId() + ", securities=" + this.getSecurities() + ", eventID=" + this.getEventID() + ")";
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof EventXClassification))
		{
			return false;
		}
		final EventXClassification other = (EventXClassification) o;
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
		return other instanceof EventXClassification;
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
