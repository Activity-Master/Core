package com.guicedee.activitymaster.core.db.entities.systems;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.systems.builders.SystemsSecurityTokenQueryBuilder;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "SystemsSecurityToken")
@XmlRootElement

@Access(FIELD)
public class SystemsSecurityToken
		extends WarehouseSecurityTable<SystemsSecurityToken, SystemsSecurityTokenQueryBuilder, java.util.UUID>
{

	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "SystemsSecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@JoinColumn(name = "SystemID",
			referencedColumnName = "SystemID",
			nullable = false,
			updatable = false,
			insertable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Systems base;

	public SystemsSecurityToken()
	{

	}

	public SystemsSecurityToken(UUID systemsSecurityTokenID)
	{
		this.id = systemsSecurityTokenID;
	}

	public String toString()
	{
		return "SystemsSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public Systems getBase()
	{
		return this.base;
	}

	public SystemsSecurityToken setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public SystemsSecurityToken setBase(Systems base)
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
		if (!(o instanceof SystemsSecurityToken))
		{
			return false;
		}
		final SystemsSecurityToken other = (SystemsSecurityToken) o;
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
		return other instanceof SystemsSecurityToken;
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
