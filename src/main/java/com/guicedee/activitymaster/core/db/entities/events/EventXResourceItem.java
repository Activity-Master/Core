package com.guicedee.activitymaster.core.db.entities.events;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventXResourceItemQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IEvent;
import com.guicedee.activitymaster.core.services.dto.IResourceItem;
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
@Table(schema="Event",name = "EventXResourceItem")
@XmlRootElement

@Access(FIELD)
public class EventXResourceItem
		extends WarehouseClassificationRelationshipTable<Event,
				                                                ResourceItem,
				                                                EventXResourceItem,
				                                                EventXResourceItemQueryBuilder,
				                                                Long,
				                                                EventXResourceItemSecurityToken,
				                                                IEvent<?>, IResourceItem<?>>
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
	protected EventXResourceItemSecurityToken configureDefaultsForNewToken(EventXResourceItemSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
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
		EventXResourceItem that = (EventXResourceItem) o;
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
	public IResourceItem<?> getSecondary()
	{
		return getResourceItemID();
	}
}
