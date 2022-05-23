package com.guicedee.activitymaster.fsdm.db.entities.security;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.security.builders.SecurityTokenXSecurityTokenSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;


/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Security", name = "SecurityTokensXSecurityTokenSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class SecurityTokenXSecurityTokenSecurityToken
		extends WarehouseSecurityTable<SecurityTokenXSecurityTokenSecurityToken, SecurityTokenXSecurityTokenSecurityTokenQueryBuilder, java.lang.String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "SecurityTokenXSecurityTokenSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "SecurityTokenXSecurityTokenID",
	            referencedColumnName = "SecurityTokenXSecurityTokenID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private SecurityTokenXSecurityToken base;
	
	public SecurityTokenXSecurityTokenSecurityToken()
	{
	
	}
	
	public SecurityTokenXSecurityTokenSecurityToken(java.lang.String resourceItemDataSecurityTokenID)
	{
		this.id = resourceItemDataSecurityTokenID;
	}
	
	public String toString()
	{
		return "SecurityTokenXSecurityTokenSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public SecurityTokenXSecurityTokenSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public SecurityTokenXSecurityToken getBase()
	{
		return this.base;
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
