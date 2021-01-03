package com.guicedee.activitymaster.core.db.entities.events;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.events.builders.EventXRulesQueryBuilder;

import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IEvent;
import com.guicedee.activitymaster.core.services.dto.IRules;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.FIELD;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Event", name = "EventXRules")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class EventXRules
		extends WarehouseClassificationRelationshipTable<Event,
		Rules,
		EventXRules,
		EventXRulesQueryBuilder,
		UUID,
		EventXRulesSecurityToken,
		IEvent<?>, IRules<?>>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EventXRulesID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<EventXRulesSecurityToken> securities;
	@JoinColumn(name = "EventID",
	            referencedColumnName = "EventID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Event eventID;
	@JoinColumn(name = "RulesID",
	            referencedColumnName = "RulesID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Rules rulesID;
	
	public EventXRules()
	{
	
	}
	
	public EventXRules(UUID eventXRulesID)
	{
		this.id = eventXRulesID;
	}
	
	@Override
	protected EventXRulesSecurityToken configureDefaultsForNewToken(EventXRulesSecurityToken stAdmin,  ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public List<EventXRulesSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public Event getEventID()
	{
		return this.eventID;
	}
	
	public Rules getRulesID()
	{
		return this.rulesID;
	}
	
	public EventXRules setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public EventXRules setSecurities(List<EventXRulesSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public EventXRules setEventID(Event eventID)
	{
		this.eventID = eventID;
		return this;
	}
	
	public EventXRules setRulesID(Rules resourceItemID)
	{
		this.rulesID = resourceItemID;
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
		EventXRules that = (EventXRules) o;
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
	public IRules<?> getSecondary()
	{
		return getRulesID();
	}
}
