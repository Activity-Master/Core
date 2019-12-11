/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.arrangement;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementTypeSecurityTokenQueryBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Arrangement",name = "ArrangementTypeSecurityToken")
@XmlRootElement

@Access(FIELD)
public class ArrangementTypeSecurityToken
		extends WarehouseSecurityTable<ArrangementTypeSecurityToken, ArrangementTypeSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ArrangementTypeSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ArrangementTypeID",
			referencedColumnName = "ArrangementTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ArrangementType base;

	public ArrangementTypeSecurityToken()
	{

	}

	public ArrangementTypeSecurityToken(Long arrangementTypeSecurityTokenID)
	{
		this.id = arrangementTypeSecurityTokenID;
	}

	public String toString()
	{
		return "ArrangementTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public ArrangementType getBase()
	{
		return this.base;
	}

	public ArrangementTypeSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ArrangementTypeSecurityToken setBase(ArrangementType base)
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
		if (!(o instanceof ArrangementTypeSecurityToken))
		{
			return false;
		}
		final ArrangementTypeSecurityToken other = (ArrangementTypeSecurityToken) o;
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
		return other instanceof ArrangementTypeSecurityToken;
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
