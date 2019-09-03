package com.armineasy.activitymaster.activitymaster.db.entities.security;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.security.builders.SecurityTokensSecurityTokenQueryBuilder;


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
@Table(name = "SecurityTokensSecurityToken")
@XmlRootElement

@Access(FIELD)
public class SecurityTokensSecurityToken
		extends WarehouseSecurityTable<SecurityTokensSecurityToken, SecurityTokensSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "SecurityTokenAccessID")
	private Long id;

	@JoinColumn(name = "SecurityTokenToID",
			referencedColumnName = "SecurityTokenID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private SecurityToken base;

	public SecurityTokensSecurityToken()
	{

	}

	public SecurityTokensSecurityToken(Long securityTokenAccessID)
	{
		this.id = securityTokenAccessID;
	}

	public String toString()
	{
		return "SecurityTokensSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public SecurityToken getBase()
	{
		return this.base;
	}

	public SecurityTokensSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public SecurityTokensSecurityToken setBase(SecurityToken base)
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
		if (!(o instanceof SecurityTokensSecurityToken))
		{
			return false;
		}
		final SecurityTokensSecurityToken other = (SecurityTokensSecurityToken) o;
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
		return other instanceof SecurityTokensSecurityToken;
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
