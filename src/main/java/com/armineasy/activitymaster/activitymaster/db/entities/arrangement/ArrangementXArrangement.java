/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.arrangement;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementXArrangementQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
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
@Table(name = "ArrangementXArrangement")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class ArrangementXArrangement
		extends WarehouseClassificationRelationshipTable<Arrangement, Arrangement, ArrangementXArrangement, ArrangementXArrangementQueryBuilder, Long, ArrangementXArrangementSecurityToken>

{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ArrangementXArrangementID")
	private Long id;

	@JoinColumn(name = "ChildArrangementID",
			referencedColumnName = "ArrangementID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Arrangement childArrangementID;
	@JoinColumn(name = "ParentArrangementID",
			referencedColumnName = "ArrangementID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Arrangement parentArrangementID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementSecurityToken> securities;

	public ArrangementXArrangement()
	{

	}

	public ArrangementXArrangement(Long arrangementXArrangementID)
	{
		this.id = arrangementXArrangementID;
	}

	@Override
	protected ArrangementXArrangementSecurityToken configureDefaultsForNewToken(ArrangementXArrangementSecurityToken stAdmin, Enterprise enterprise, Systems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}
