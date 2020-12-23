/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.geography;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.geography.builders.GeographyXClassificationSecurityTokenQueryBuilder;


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
@Table(schema="Geography",name = "GeographyXClassificationSecurityToken")
@XmlRootElement

@Access(FIELD)
public class GeographyXClassificationSecurityToken
		extends WarehouseSecurityTable<GeographyXClassificationSecurityToken, GeographyXClassificationSecurityTokenQueryBuilder, java.util.UUID>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "GeographyXClassificationSecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	@JoinColumn(name = "GeographyXClassificationID",
			referencedColumnName = "GeographyXClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private GeographyXClassification base;

	public GeographyXClassificationSecurityToken()
	{

	}

	public GeographyXClassificationSecurityToken(UUID geographyXClassificationSecurityTokenID)
	{
		this.id = geographyXClassificationSecurityTokenID;
	}

	public String toString()
	{
		return "GeographyXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public GeographyXClassification getBase()
	{
		return this.base;
	}

	public GeographyXClassificationSecurityToken setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public GeographyXClassificationSecurityToken setBase(GeographyXClassification base)
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
		if (!(o instanceof GeographyXClassificationSecurityToken))
		{
			return false;
		}
		final GeographyXClassificationSecurityToken other = (GeographyXClassificationSecurityToken) o;
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
		return other instanceof GeographyXClassificationSecurityToken;
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
