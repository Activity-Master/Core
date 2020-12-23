/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.geography;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.geography.builders.GeographySecurityTokenQueryBuilder;


import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Geography",name = "GeographySecurityToken")
@XmlRootElement

@Access(FIELD)
public class GeographySecurityToken
		extends WarehouseSecurityTable<GeographySecurityToken, GeographySecurityTokenQueryBuilder, java.util.UUID>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "GeographySecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	@JoinColumn(name = "GeographyID",
			referencedColumnName = "GeographyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private Geography base;

	public GeographySecurityToken()
	{

	}

	public GeographySecurityToken(UUID geographySecurityTokenID)
	{
		this.id = geographySecurityTokenID;
	}

	public String toString()
	{
		return "GeographySecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public Geography getBase()
	{
		return this.base;
	}

	public GeographySecurityToken setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public GeographySecurityToken setBase(Geography base)
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
		if (!(o instanceof GeographySecurityToken))
		{
			return false;
		}
		final GeographySecurityToken other = (GeographySecurityToken) o;
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
		return other instanceof GeographySecurityToken;
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
