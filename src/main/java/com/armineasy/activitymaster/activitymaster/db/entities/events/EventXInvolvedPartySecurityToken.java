package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventXInvolvedPartySecurityTokenQueryBuilder;
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
@Table(name = "EventXInvolvedPartySecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class EventXInvolvedPartySecurityToken
		extends WarehouseSecurityTable<EventXInvolvedPartySecurityToken, EventXInvolvedPartySecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventXInvolvedPartySecurityTokenID")
	private Long id;

	@JoinColumn(name = "EventXInvolvedPartyID",
			referencedColumnName = "EventXInvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private EventXInvolvedParty base;

	public EventXInvolvedPartySecurityToken()
	{

	}

	public EventXInvolvedPartySecurityToken(Long eventXInvolvedPartySecurityTokenID)
	{
		this.id = eventXInvolvedPartySecurityTokenID;
	}
}
