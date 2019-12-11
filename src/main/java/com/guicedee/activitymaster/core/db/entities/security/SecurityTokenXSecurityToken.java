package com.guicedee.activitymaster.core.db.entities.security;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.security.builders.SecurityTokenXSecurityTokenQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISecurityToken;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Security",name = "SecurityTokenXSecurityToken")
@XmlRootElement

@Access(FIELD)
public class SecurityTokenXSecurityToken
		extends WarehouseClassificationRelationshipTable<SecurityToken,
				                                                SecurityToken,
				                                                SecurityTokenXSecurityToken,
				                                                SecurityTokenXSecurityTokenQueryBuilder,
				                                                Long,
				                                                SecurityTokenXSecurityTokenSecurityToken,
				                                                ISecurityToken<?>,ISecurityToken<?>>
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
	protected SecurityTokenXSecurityTokenSecurityToken configureDefaultsForNewToken(SecurityTokenXSecurityTokenSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
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

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		SecurityTokenXSecurityToken that = (SecurityTokenXSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public ISecurityToken<?> getPrimary()
	{
		return getParentSecurityTokenID();
	}

	@Override
	public ISecurityToken<?> getSecondary()
	{
		return getChildSecurityTokenID();
	}
}
