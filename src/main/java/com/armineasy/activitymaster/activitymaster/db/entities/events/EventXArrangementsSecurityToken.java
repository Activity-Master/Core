package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventXArrangementSecurityTokenQueryBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "EventXArrangementsSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class EventXArrangementsSecurityToken
		extends WarehouseSecurityTable<EventXArrangementsSecurityToken, EventXArrangementSecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventXArrangementsSecurityTokenID")
	private Long id;

	@JoinColumn(name = "EventXArrangementsID",
			referencedColumnName = "EventXArrangementsID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private EventXArrangement base;

	public EventXArrangementsSecurityToken()
	{

	}

	public EventXArrangementsSecurityToken(Long eventXArrangementsSecurityTokenID)
	{
		this.id = eventXArrangementsSecurityTokenID;
	}
}
