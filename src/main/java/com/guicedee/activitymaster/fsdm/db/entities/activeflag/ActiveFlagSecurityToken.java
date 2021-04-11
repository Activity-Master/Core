package com.guicedee.activitymaster.fsdm.db.entities.activeflag;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders.ActiveFlagSecurityTokenQueryBuilder;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ActiveFlagSecurityToken")
@XmlRootElement
@Access(FIELD)
public class ActiveFlagSecurityToken
		extends WarehouseSecurityTable<ActiveFlagSecurityToken, ActiveFlagSecurityTokenQueryBuilder, UUID>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
			name = "ActiveFlagSecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;

	@JoinColumn(name = "SecurityTokenActiveFlagID",
			referencedColumnName = "ActiveFlagID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL)
	private ActiveFlag base;

	public ActiveFlagSecurityToken()
	{

	}

	public ActiveFlagSecurityToken(UUID activeFlagSecurityTokenID)
	{
		this.id = activeFlagSecurityTokenID;
	}

	public String toString()
	{
		return "ActiveFlagSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public UUID getId()
	{
		return this.id;
	}

	public ActiveFlag getBase()
	{
		return this.base;
	}

	public ActiveFlagSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}

	public ActiveFlagSecurityToken setBase(ActiveFlag base)
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
		if (!(o instanceof ActiveFlagSecurityToken))
		{
			return false;
		}
		final ActiveFlagSecurityToken other = (ActiveFlagSecurityToken) o;
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
		return other instanceof ActiveFlagSecurityToken;
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
