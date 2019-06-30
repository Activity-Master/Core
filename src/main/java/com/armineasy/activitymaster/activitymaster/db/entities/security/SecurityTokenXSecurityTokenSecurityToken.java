package com.armineasy.activitymaster.activitymaster.db.entities.security;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.security.builders.SecurityTokenXSecurityTokenSecurityTokenQueryBuilder;
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
@Table(name = "SecurityTokensXSecurityTokenSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class SecurityTokenXSecurityTokenSecurityToken
		extends WarehouseSecurityTable<SecurityTokenXSecurityTokenSecurityToken, SecurityTokenXSecurityTokenSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "SecurityTokenXSecurityTokenSecurityTokenID")
	private Long id;

	@JoinColumn(name = "SecurityTokenXSecurityTokenID",
			referencedColumnName = "SecurityTokenXSecurityTokenID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private SecurityTokenXSecurityToken base;

	public SecurityTokenXSecurityTokenSecurityToken()
	{

	}

	public SecurityTokenXSecurityTokenSecurityToken(Long resourceItemDataSecurityTokenID)
	{
		this.id = resourceItemDataSecurityTokenID;
	}

	public String toString()
	{
		return "SecurityTokenXSecurityTokenSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public SecurityTokenXSecurityToken getBase()
	{
		return this.base;
	}

	public SecurityTokenXSecurityTokenSecurityToken setId(Long id)
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
