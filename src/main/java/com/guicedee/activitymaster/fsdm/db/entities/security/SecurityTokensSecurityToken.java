package com.guicedee.activitymaster.fsdm.db.entities.security;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.security.builders.SecurityTokensSecurityTokenQueryBuilder;
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
@Table(schema = "Security", name = "SecurityTokensSecurityToken")
@XmlRootElement

@Access(FIELD)
public class SecurityTokensSecurityToken
		extends WarehouseSecurityTable<SecurityTokensSecurityToken, SecurityTokensSecurityTokenQueryBuilder, UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	
	@Column(nullable = false,
	        name = "SecurityTokenAccessID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
	@JoinColumn(name = "SecurityTokenToID",
	            referencedColumnName = "SecurityTokenID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private SecurityToken base;
	
	public SecurityTokensSecurityToken()
	{
	
	}
	
	public SecurityTokensSecurityToken(UUID securityTokenAccessID)
	{
		this.id = securityTokenAccessID;
	}
	
	public String toString()
	{
		return "SecurityTokensSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public SecurityTokensSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public SecurityToken getBase()
	{
		return this.base;
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
