package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventTypeQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IEventType;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "EventType")
@XmlRootElement
@Accessors(chain = true)
@EqualsAndHashCode(of = "id",
		callSuper = false)
@Access(FIELD)@lombok.Data
public class EventType
		extends WarehouseSCDNameDescriptionTable<EventType, EventTypeQueryBuilder, Long, EventTypesSecurityToken>
		implements IEventType<EventType>,
				           IActivityMasterEntity<EventType>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventTypeID")
	@Getter
	@Setter
	private Long id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 200)
	@Column(nullable = false,
			length = 200,
			name = "EventTypeName")
	@Getter
	@Setter
	private String name;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 200)
	@Column(nullable = false,
			length = 200,
			name = "EventTypeDesc")
	@Getter
	@Setter
	private String description;

	@OneToMany(
			mappedBy = "eventTypeID",
			fetch = FetchType.LAZY)
	private List<EventXEventType> eventXEventTypeList;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<EventTypesSecurityToken> securities;

	public EventType()
	{

	}

	public EventType(Long eventTypeID)
	{
		this.id = eventTypeID;
	}

	public EventType(Long eventTypeID, String eventTypName, String eventTypeDesc)
	{
		this.id = eventTypeID;
		this.name = eventTypName;
		this.description = eventTypeDesc;
	}

	@Override
	protected EventTypesSecurityToken configureDefaultsForNewToken(EventTypesSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}
