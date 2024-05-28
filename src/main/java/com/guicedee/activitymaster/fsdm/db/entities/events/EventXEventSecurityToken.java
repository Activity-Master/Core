package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXEventSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Event", name = "EventXEventSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class EventXEventSecurityToken
		extends WarehouseSecurityTable<EventXEventSecurityToken, EventXEventSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXEventSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "EventXEventID",
	            referencedColumnName = "EventXEventID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private EventXEvent base;
	
	public EventXEventSecurityToken()
	{
	
	}
	
	public EventXEventSecurityToken(java.lang.String productXEventSecurityTokenID)
	{
		this.id = productXEventSecurityTokenID;
	}
	
	public String toString()
	{
		return "EventXEventSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public EventXEventSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public EventXEvent getBase()
	{
		return this.base;
	}
	
	public EventXEventSecurityToken setBase(EventXEvent base)
	{
		this.base = base;
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
		EventXEventSecurityToken that = (EventXEventSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
