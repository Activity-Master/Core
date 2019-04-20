/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "InvolvedPartyXInvolvedParty")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class InvolvedPartyXInvolvedParty
		extends WarehouseClassificationRelationshipTable<InvolvedParty, InvolvedParty, InvolvedPartyXInvolvedParty, InvolvedPartyXInvolvedPartyQueryBuilder, Long, InvolvedPartyXInvolvedPartySecurityToken>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyXInvolvedPartyID")
	private Long id;

	@JoinColumn(name = "ChildInvolvedPartyID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedParty childInvolvedPartyID;
	@JoinColumn(name = "ParentInvolvedPartyID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedParty parentInvolvedPartyID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartySecurityToken> securities;

	public InvolvedPartyXInvolvedParty()
	{

	}

	public InvolvedPartyXInvolvedParty(Long involvedPartyXInvolvedPartyID)
	{
		this.id = involvedPartyXInvolvedPartyID;
	}

	@Override
	protected InvolvedPartyXInvolvedPartySecurityToken configureDefaultsForNewToken(InvolvedPartyXInvolvedPartySecurityToken stAdmin, Enterprise enterprise, Systems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}
