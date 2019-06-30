package com.armineasy.activitymaster.activitymaster.db.entities.enterprise;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.builders.EnterpriseSecurityTokenQueryBuilder;
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
@Table(name = "EnterpriseSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class EnterpriseSecurityToken
		extends WarehouseSecurityTable<EnterpriseSecurityToken, EnterpriseSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EnterpriseSecurityTokenID")
	private Long id;

	@JoinColumn(name = "EnterpriseID",
			referencedColumnName = "EnterpriseID",
			nullable = false,insertable = false,updatable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Enterprise base;

	public EnterpriseSecurityToken()
	{

	}

	public EnterpriseSecurityToken(Long enterpriseSecurityTokenID)
	{
		this.id = enterpriseSecurityTokenID;
	}

	public String toString()
	{
		return "EnterpriseSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public Enterprise getBase()
	{
		return this.base;
	}

	public EnterpriseSecurityToken setId(Long id)
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
