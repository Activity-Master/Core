package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventXResourceItemQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
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
@Table(name = "EventXResourceItem")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class EventXResourceItem
		extends WarehouseClassificationRelationshipTable<Event, ResourceItem, EventXResourceItem, EventXResourceItemQueryBuilder, Long, EventXResourceItemSecurityToken>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventXResourceItemID")
	private Long id;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<EventXResourceItemSecurityToken> securities;
	@JoinColumn(name = "EventID",
			referencedColumnName = "EventID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Event eventID;
	@JoinColumn(name = "ResourceItemID",
			referencedColumnName = "ResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ResourceItem resourceItemID;

	public EventXResourceItem()
	{

	}

	public EventXResourceItem(Long eventXResourceItemID)
	{
		this.id = eventXResourceItemID;
	}

	@Override
	protected EventXResourceItemSecurityToken configureDefaultsForNewToken(EventXResourceItemSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "EventXResourceItem(id=" + this.getId() + ", securities=" + this.getSecurities() + ", eventID=" + this.getEventID() + ", resourceItemID=" +
		       this.getResourceItemID() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public List<EventXResourceItemSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public Event getEventID()
	{
		return this.eventID;
	}

	public ResourceItem getResourceItemID()
	{
		return this.resourceItemID;
	}

	public EventXResourceItem setId(Long id)
	{
		this.id = id;
		return this;
	}

	public EventXResourceItem setSecurities(List<EventXResourceItemSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public EventXResourceItem setEventID(Event eventID)
	{
		this.eventID = eventID;
		return this;
	}

	public EventXResourceItem setResourceItemID(ResourceItem resourceItemID)
	{
		this.resourceItemID = resourceItemID;
		return this;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof EventXResourceItem))
		{
			return false;
		}
		final EventXResourceItem other = (EventXResourceItem) o;
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
		return other instanceof EventXResourceItem;
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
