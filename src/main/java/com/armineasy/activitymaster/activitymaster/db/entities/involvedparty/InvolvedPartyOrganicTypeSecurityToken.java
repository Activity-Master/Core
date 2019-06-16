/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyOrganicTypeSecurityTokenQueryBuilder;
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
@Table(name = "InvolvedPartyOrganicTypeSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
@Access(FIELD)@lombok.Data
public class InvolvedPartyOrganicTypeSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyOrganicTypeSecurityToken, InvolvedPartyOrganicTypeSecurityTokenQueryBuilder, Long>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyOrganicTypeSecurityTokenID")
	private Long id;

	@JoinColumn(name = "InvolvedPartyOrganicTypeID",
			referencedColumnName = "InvolvedPartyOrganicTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private InvolvedPartyOrganicType base;

	public InvolvedPartyOrganicTypeSecurityToken()
	{

	}

	public InvolvedPartyOrganicTypeSecurityToken(Long involvedPartyOrganicTypeSecurityTokenID)
	{
		this.id = involvedPartyOrganicTypeSecurityTokenID;
	}
}
