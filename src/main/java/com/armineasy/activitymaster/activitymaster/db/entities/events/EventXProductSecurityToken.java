/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventXProductSecurityTokenQueryBuilder;
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
@Table(name = "EventXProductSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class EventXProductSecurityToken
		extends WarehouseSecurityTable<EventXProductSecurityToken, EventXProductSecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventXProductSecurityTokenID")
	private Long id;

	@JoinColumn(name = "EventXProductID",
			referencedColumnName = "EventXProductID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private EventXProduct base;

	public EventXProductSecurityToken()
	{

	}

	public EventXProductSecurityToken(Long eventXProductSecurityTokenID)
	{
		this.id = eventXProductSecurityTokenID;
	}
}
