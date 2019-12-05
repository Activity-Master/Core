/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.geography;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.geography.builders.GeographyXGeographyQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IGeography;
import com.guicedee.activitymaster.core.services.dto.ISystems;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "GeographyXGeography")
@XmlRootElement

@Access(FIELD)
public class GeographyXGeography
		extends WarehouseClassificationRelationshipTable<Geography,
				                                                Geography,
				                                                GeographyXGeography,
				                                                GeographyXGeographyQueryBuilder,
				                                                Long,
				                                                GeographyXGeographySecurityToken,
				                                                IGeography<?>,IGeography<?>>
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
	protected GeographyXGeographySecurityToken configureDefaultsForNewToken(GeographyXGeographySecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
	{
		return this.id;
	}

	public List<GeographyXGeographySecurityToken> getSecurities()
	{
		return this.securities;
	}

	public Geography getParentGeographyID()
	{
		return this.parentGeographyID;
	}

	public Geography getChildGeographyID()
	{
		return this.childGeographyID;
	}

	public GeographyXGeography setId(Long id)
	{
		this.id = id;
		return this;
	}

	public GeographyXGeography setSecurities(List<GeographyXGeographySecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public GeographyXGeography setParentGeographyID(Geography parentGeographyID)
	{
		this.parentGeographyID = parentGeographyID;
		return this;
	}

	public GeographyXGeography setChildGeographyID(Geography childGeographyID)
	{
		this.childGeographyID = childGeographyID;
		return this;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		GeographyXGeography that = (GeographyXGeography) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IGeography<?> getPrimary()
	{
		return getParentGeographyID();
	}

	@Override
	public IGeography<?> getSecondary()
	{
		return getChildGeographyID();
	}
}
