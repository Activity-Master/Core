/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartySecurityTokenQueryBuilder;
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
@Table(name = "InvolvedPartyXInvolvedPartySecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class InvolvedPartyXInvolvedPartySecurityToken
		extends WarehouseSecurityTable<InvolvedPartyXInvolvedPartySecurityToken, InvolvedPartyXInvolvedPartySecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyXInvolvedPartySecurityTokenID")
	private Long id;
	@JoinColumn(name = "InvolvedPartyXInvolvedPartyID",
			referencedColumnName = "InvolvedPartyXInvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private InvolvedPartyXInvolvedParty base;

	public InvolvedPartyXInvolvedPartySecurityToken()
	{

	}

	public InvolvedPartyXInvolvedPartySecurityToken(Long involvedPartyXInvolvedPartySecurityTokenID)
	{
		this.id = involvedPartyXInvolvedPartySecurityTokenID;
	}
}
