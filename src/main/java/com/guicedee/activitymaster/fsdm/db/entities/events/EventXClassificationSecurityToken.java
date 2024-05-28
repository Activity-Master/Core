package com.guicedee.activitymaster.fsdm.db.entities.events;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXClassificationSecurityTokenQueryBuilder;
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
@Table(schema = "Event", name = "EventXClassificationSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class EventXClassificationSecurityToken
		extends WarehouseSecurityTable<EventXClassificationSecurityToken, EventXClassificationSecurityTokenQueryBuilder, String>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXClassificationsSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "EventXClassificationsID",
	            referencedColumnName = "EventXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private EventXClassification base;
	
	public EventXClassificationSecurityToken()
	{
	
	}
	
	public EventXClassificationSecurityToken(java.lang.String eventXClassificationsSecurityTokenID)
	{
		this.id = eventXClassificationsSecurityTokenID;
	}
	
	public String toString()
	{
		return "EventXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public EventXClassificationSecurityToken setId(java.lang.String id)
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
		EventXClassificationSecurityToken that = (EventXClassificationSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
