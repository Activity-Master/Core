package com.guicedee.activitymaster.core.db.entities.enterprise;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.enterprise.builders.EnterpriseSecurityTokenQueryBuilder;

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
@Table(name = "EnterpriseSecurityToken")
@XmlRootElement

@Access(FIELD)
public class EnterpriseSecurityToken
		extends WarehouseSecurityTable<EnterpriseSecurityToken, EnterpriseSecurityTokenQueryBuilder, java.util.UUID>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "EnterpriseSecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@JoinColumn(name = "EnterpriseID",
			referencedColumnName = "EnterpriseID",
			nullable = false,insertable = false,updatable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Enterprise base;

	public EnterpriseSecurityToken()
	{

	}

	public EnterpriseSecurityToken(UUID enterpriseSecurityTokenID)
	{
		this.id = enterpriseSecurityTokenID;
	}

	public String toString()
	{
		return "EnterpriseSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public Enterprise getBase()
	{
		return this.base;
	}

	public EnterpriseSecurityToken setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public EnterpriseSecurityToken setBase(Enterprise base)
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
		if (!(o instanceof EnterpriseSecurityToken))
		{
			return false;
		}
		final EnterpriseSecurityToken other = (EnterpriseSecurityToken) o;
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
		return other instanceof EnterpriseSecurityToken;
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
