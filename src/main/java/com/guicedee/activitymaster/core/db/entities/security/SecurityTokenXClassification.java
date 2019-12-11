package com.guicedee.activitymaster.core.db.entities.security;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.security.builders.SecurityTokenXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISecurityToken;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Security",name = "SecurityTokenXClassification")
@XmlRootElement

@Access(FIELD)
public class SecurityTokenXClassification
		extends WarehouseClassificationRelationshipTable<SecurityToken,
				                                                Classification,
				                                                SecurityTokenXClassification,
				                                                SecurityTokenXClassificationQueryBuilder,
				                                                Long,
				                                                SecurityTokenXClassificationSecurityToken,
				                                                ISecurityToken<?>, IClassification<?>>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "SecurityTokenXClassificationID")
	private Long id;

	@JoinColumn(name = "SecurityTokenID",
			referencedColumnName = "SecurityTokenID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private SecurityToken securityTokenID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<SecurityTokenXClassificationSecurityToken> securities;

	public SecurityTokenXClassification()
	{

	}

	public SecurityTokenXClassification(Long securityTokenXClassificationID)
	{
		this.id = securityTokenXClassificationID;
	}

	@Override
	protected SecurityTokenXClassificationSecurityToken configureDefaultsForNewToken(SecurityTokenXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
	{
		return this.id;
	}

	public SecurityToken getSecurityTokenID()
	{
		return this.securityTokenID;
	}

	public List<SecurityTokenXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public SecurityTokenXClassification setId(Long id)
	{
		this.id = id;
		return this;
	}

	public SecurityTokenXClassification setSecurityTokenID(SecurityToken securityTokenID)
	{
		this.securityTokenID = securityTokenID;
		return this;
	}

	public SecurityTokenXClassification setSecurities(List<SecurityTokenXClassificationSecurityToken> securities)
	{
		this.securities = securities;
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
		SecurityTokenXClassification that = (SecurityTokenXClassification) o;
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
		return getSecurityTokenID();
	}

	@Override
	public IClassification<?> getSecondary()
	{
		return getClassificationID();
	}
}
