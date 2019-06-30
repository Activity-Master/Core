/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.arrangement;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementXArrangementSecurityTokenQueryBuilder;
import lombok.experimental.Accessors;

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
@Table(name = "ArrangementXArrangementSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ArrangementXArrangementSecurityToken
		extends WarehouseSecurityTable<ArrangementXArrangementSecurityToken, ArrangementXArrangementSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ArrangementXArrangementSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ArrangementXArrangementID",
			referencedColumnName = "ArrangementXArrangementID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ArrangementXArrangement base;

	public ArrangementXArrangementSecurityToken()
	{

	}

	public ArrangementXArrangementSecurityToken(Long arrangementXArrangementSecurityTokenID)
	{
		this.id = arrangementXArrangementSecurityTokenID;
	}

	public String toString()
	{
		return "ArrangementXArrangementSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public ArrangementXArrangement getBase()
	{
		return this.base;
	}

	public ArrangementXArrangementSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ArrangementXArrangementSecurityToken setBase(ArrangementXArrangement base)
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
		if (!(o instanceof ArrangementXArrangementSecurityToken))
		{
			return false;
		}
		final ArrangementXArrangementSecurityToken other = (ArrangementXArrangementSecurityToken) o;
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
		return other instanceof ArrangementXArrangementSecurityToken;
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
