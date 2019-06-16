/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.events;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.events.builders.EventXResourceItemSecurityTokenQueryBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "EventXResourceItemSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
@Access(FIELD)@lombok.Data
public class EventXResourceItemSecurityToken
		extends WarehouseSecurityTable<EventXResourceItemSecurityToken, EventXResourceItemSecurityTokenQueryBuilder, Long>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EventXResourceItemSecurityTokenID")
	private Long id;

	@JoinColumn(name = "EventXResourceItemID",
			referencedColumnName = "EventXResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private EventXResourceItem base;

	public EventXResourceItemSecurityToken()
	{

	}

	public EventXResourceItemSecurityToken(Long eventXResourceItemSecurityTokenID)
	{
		this.id = eventXResourceItemSecurityTokenID;
	}
}
