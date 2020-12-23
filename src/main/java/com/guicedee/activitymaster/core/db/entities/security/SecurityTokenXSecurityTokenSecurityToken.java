package com.guicedee.activitymaster.core.db.entities.security;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.security.builders.SecurityTokenXSecurityTokenSecurityTokenQueryBuilder;

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
@Table(schema="Security",name = "SecurityTokensXSecurityTokenSecurityToken")
@XmlRootElement

@Access(FIELD)
public class SecurityTokenXSecurityTokenSecurityToken
		extends WarehouseSecurityTable<SecurityTokenXSecurityTokenSecurityToken, SecurityTokenXSecurityTokenSecurityTokenQueryBuilder, java.util.UUID>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "SecurityTokenXSecurityTokenSecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@JoinColumn(name = "SecurityTokenXSecurityTokenID",
			referencedColumnName = "SecurityTokenXSecurityTokenID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private SecurityTokenXSecurityToken base;

	public SecurityTokenXSecurityTokenSecurityToken()
	{

	}

	public SecurityTokenXSecurityTokenSecurityToken(UUID resourceItemDataSecurityTokenID)
	{
		this.id = resourceItemDataSecurityTokenID;
	}

	public String toString()
	{
		return "SecurityTokenXSecurityTokenSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public SecurityTokenXSecurityToken getBase()
	{
		return this.base;
	}

	public SecurityTokenXSecurityTokenSecurityToken setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public SecurityTokenXSecurityTokenSecurityToken setBase(SecurityTokenXSecurityToken base)
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
		if (!(o instanceof SecurityTokenXSecurityTokenSecurityToken))
		{
			return false;
		}
		final SecurityTokenXSecurityTokenSecurityToken other = (SecurityTokenXSecurityTokenSecurityToken) o;
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
		return other instanceof SecurityTokenXSecurityTokenSecurityToken;
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
