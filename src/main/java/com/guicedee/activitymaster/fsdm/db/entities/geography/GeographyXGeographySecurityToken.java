/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.geography;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.geography.builders.GeographyXGeographySecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Geography", name = "GeographyXGeographySecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class GeographyXGeographySecurityToken
		extends WarehouseSecurityTable<GeographyXGeographySecurityToken, GeographyXGeographySecurityTokenQueryBuilder, java.lang.String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "GeographyXGeographySecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "GeographyXGeographyID",
	            referencedColumnName = "GeographyXGeographyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private GeographyXGeography base;
	
	public GeographyXGeographySecurityToken()
	{
	
	}
	
	public GeographyXGeographySecurityToken(java.lang.String geographyXGeographySecurityTokenID)
	{
		this.id = geographyXGeographySecurityTokenID;
	}
	
	public String toString()
	{
		return "GeographyXGeographySecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public GeographyXGeographySecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public GeographyXGeography getBase()
	{
		return this.base;
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
		if (!other.canEqual(this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		return this$id == null ? other$id == null : this$id.equals(other$id);
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
