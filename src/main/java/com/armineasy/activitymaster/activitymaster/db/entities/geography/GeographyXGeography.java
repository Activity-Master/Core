/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.geography;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.builders.GeographyXGeographyQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "GeographyXGeography")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
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
	protected GeographyXGeographySecurityToken configureDefaultsForNewToken(GeographyXGeographySecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "GeographyXGeography(id=" + this.getId() + ", securities=" + this.getSecurities() + ", parentGeographyID=" + this.getParentGeographyID() + ", childGeographyID=" +
		       this.getChildGeographyID() + ")";
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof GeographyXGeography))
		{
			return false;
		}
		final GeographyXGeography other = (GeographyXGeography) o;
		if (!other.canEqual((Object) this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
		{
			return false;
		}
		return true;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof GeographyXGeography;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}
