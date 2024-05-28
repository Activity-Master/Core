package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXArrangementSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.Objects;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Event", name = "EventXArrangementsSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class EventXArrangementsSecurityToken
		extends WarehouseSecurityTable<EventXArrangementsSecurityToken, EventXArrangementSecurityTokenQueryBuilder, String>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXArrangementsSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "EventXArrangementsID",
	            referencedColumnName = "EventXArrangementsID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private EventXArrangement base;
	
	public EventXArrangementsSecurityToken()
	{
	
	}
	
	public EventXArrangementsSecurityToken(java.lang.String eventXArrangementsSecurityTokenID)
	{
		this.id = eventXArrangementsSecurityTokenID;
	}
	
	public String toString()
	{
		return "EventXArrangementsSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public EventXArrangementsSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public EventXArrangementsSecurityToken setBase(EventXArrangement base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public EventXArrangement getBase()
	{
		return base;
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
		EventXArrangementsSecurityToken that = (EventXArrangementsSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
