/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyTypeSecurityTokenQueryBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "InvolvedPartyXInvolvedPartyTypeSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class InvolvedPartyXInvolvedPartyTypeSecurityToken
		extends WarehouseSecurityTable<InvolvedPartyXInvolvedPartyTypeSecurityToken, InvolvedPartyXInvolvedPartyTypeSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyXInvolvedPartyTypeSecurityTokenID")
	private Long id;
	@JoinColumn(name = "InvolvedPartyXInvolvedPartyTypeID",
			referencedColumnName = "InvolvedPartyXInvolvedPartyTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private InvolvedPartyXInvolvedPartyType base;

	public InvolvedPartyXInvolvedPartyTypeSecurityToken()
	{

	}

	public InvolvedPartyXInvolvedPartyTypeSecurityToken(Long involvedPartyXInvolvedPartyTypeSecurityTokenID)
	{
		this.id = involvedPartyXInvolvedPartyTypeSecurityTokenID;
	}
}
