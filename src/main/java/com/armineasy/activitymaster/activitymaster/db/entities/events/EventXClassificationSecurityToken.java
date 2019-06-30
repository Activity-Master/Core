package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventXClassificationSecurityTokenQueryBuilder;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "EventXClassificationSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class EventXClassificationSecurityToken
		extends WarehouseSecurityTable<EventXClassificationSecurityToken, EventXClassificationSecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventXClassificationsSecurityTokenID")
	private Long id;

	@JoinColumn(name = "EventXClassificationsID",
			referencedColumnName = "EventXClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private EventXClassification base;

	public EventXClassificationSecurityToken()
	{

	}

	public EventXClassificationSecurityToken(Long eventXClassificationsSecurityTokenID)
	{
		this.id = eventXClassificationsSecurityTokenID;
	}

	public String toString()
	{
		return "EventXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public EventXClassification getBase()
	{
		return this.base;
	}

	public EventXClassificationSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public EventXClassificationSecurityToken setBase(EventXClassification base)
	{
		this.base = base;
		return this;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof EventXClassificationSecurityToken))
		{
			return false;
		}
		final EventXClassificationSecurityToken other = (EventXClassificationSecurityToken) o;
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
		return other instanceof EventXClassificationSecurityToken;
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
