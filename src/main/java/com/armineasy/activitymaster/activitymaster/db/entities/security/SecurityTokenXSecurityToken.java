package com.armineasy.activitymaster.activitymaster.db.entities.security;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.security.builders.SecurityTokenXSecurityTokenQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
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
@Table(name = "SecurityTokenXSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class SecurityTokenXSecurityToken
		extends WarehouseClassificationRelationshipTable<SecurityToken, SecurityToken, SecurityTokenXSecurityToken, SecurityTokenXSecurityTokenQueryBuilder, Long, SecurityTokenXSecurityTokenSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "SecurityTokenXSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ParentSecurityTokenID",
			referencedColumnName = "SecurityTokenID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private SecurityToken parentSecurityTokenID;

	@JoinColumn(name = "ChildSecurityTokenID",
			referencedColumnName = "SecurityTokenID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private SecurityToken childSecurityTokenID;

	public SecurityTokenXSecurityToken()
	{

	}

	public SecurityTokenXSecurityToken(Long securityTokenXSecurityTokenID)
	{
		this.id = securityTokenXSecurityTokenID;
	}


	@Override
	protected SecurityTokenXSecurityTokenSecurityToken configureDefaultsForNewToken(SecurityTokenXSecurityTokenSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "SecurityTokenXSecurityToken(id=" + this.getId() + ", parentSecurityTokenID=" + this.getParentSecurityTokenID() + ", childSecurityTokenID=" +
		       this.getChildSecurityTokenID() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public SecurityToken getParentSecurityTokenID()
	{
		return this.parentSecurityTokenID;
	}

	public SecurityToken getChildSecurityTokenID()
	{
		return this.childSecurityTokenID;
	}

	public SecurityTokenXSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public SecurityTokenXSecurityToken setParentSecurityTokenID(SecurityToken parentSecurityTokenID)
	{
		this.parentSecurityTokenID = parentSecurityTokenID;
		return this;
	}

	public SecurityTokenXSecurityToken setChildSecurityTokenID(SecurityToken childSecurityTokenID)
	{
		this.childSecurityTokenID = childSecurityTokenID;
		return this;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof SecurityTokenXSecurityToken))
		{
			return false;
		}
		final SecurityTokenXSecurityToken other = (SecurityTokenXSecurityToken) o;
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
		return other instanceof SecurityTokenXSecurityToken;
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
