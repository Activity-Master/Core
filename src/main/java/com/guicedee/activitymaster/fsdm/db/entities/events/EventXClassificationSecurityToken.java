package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXClassificationSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.UUID;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Event", name = "EventXClassificationSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class EventXClassificationSecurityToken
		extends WarehouseSecurityTable<EventXClassificationSecurityToken, EventXClassificationSecurityTokenQueryBuilder, UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXClassificationsSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
	@JoinColumn(name = "EventXClassificationsID",
	            referencedColumnName = "EventXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private EventXClassification base;
	
	public EventXClassificationSecurityToken()
	{
	
	}
	
	public EventXClassificationSecurityToken(UUID eventXClassificationsSecurityTokenID)
	{
		this.id = eventXClassificationsSecurityTokenID;
	}
	
	public String toString()
	{
		return "EventXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public EventXClassificationSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public EventXClassification getBase()
	{
		return this.base;
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
		if (!other.canEqual(this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		return this$id == null ? other$id == null : this$id.equals(other$id);
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
