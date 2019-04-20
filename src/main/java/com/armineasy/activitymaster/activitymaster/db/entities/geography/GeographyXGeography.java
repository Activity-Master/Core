/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.geography;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.builders.GeographyXGeographyQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "GeographyXGeography")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class GeographyXGeography
		extends WarehouseClassificationRelationshipTable<Geography, Geography, GeographyXGeography, GeographyXGeographyQueryBuilder, Long, GeographyXGeographySecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "GeographyXGeographyID")
	private Long id;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<GeographyXGeographySecurityToken> securities;

	@JoinColumn(name = "ParentGeographyID",
			referencedColumnName = "GeographyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Geography parentGeographyID;
	@JoinColumn(name = "ChildGeographyID",
			referencedColumnName = "GeographyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Geography childGeographyID;

	public GeographyXGeography()
	{

	}

	public GeographyXGeography(Long geographyXGeographyID)
	{
		this.id = geographyXGeographyID;
	}

	@Override
	protected GeographyXGeographySecurityToken configureDefaultsForNewToken(GeographyXGeographySecurityToken stAdmin, Enterprise enterprise, Systems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}
