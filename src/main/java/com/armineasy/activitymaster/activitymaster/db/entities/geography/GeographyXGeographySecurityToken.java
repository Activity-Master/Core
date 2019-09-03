/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.geography;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.builders.GeographyXGeographySecurityTokenQueryBuilder;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "GeographyXGeographySecurityToken")
@XmlRootElement

@Access(FIELD)
public class GeographyXGeographySecurityToken
		extends WarehouseSecurityTable<GeographyXGeographySecurityToken, GeographyXGeographySecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "GeographyXGeographySecurityTokenID")
	private Long id;

	@JoinColumn(name = "GeographyXGeographyID",
			referencedColumnName = "GeographyXGeographyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private GeographyXGeography base;

	public GeographyXGeographySecurityToken()
	{

	}

	public GeographyXGeographySecurityToken(Long geographyXGeographySecurityTokenID)
	{
		this.id = geographyXGeographySecurityTokenID;
	}

	public String toString()
	{
		return "GeographyXGeographySecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public GeographyXGeography getBase()
	{
		return this.base;
	}

	public GeographyXGeographySecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public GeographyXGeographySecurityToken setBase(GeographyXGeography base)
	{
		this.base = base;
		return this;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof GeographyXGeographySecurityToken))
		{
			return false;
		}
		final GeographyXGeographySecurityToken other = (GeographyXGeographySecurityToken) o;
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
		return other instanceof GeographyXGeographySecurityToken;
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
