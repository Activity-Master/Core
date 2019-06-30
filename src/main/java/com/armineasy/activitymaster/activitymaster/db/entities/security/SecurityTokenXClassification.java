package com.armineasy.activitymaster.activitymaster.db.entities.security;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.security.builders.SecurityTokenXClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "SecurityTokenXClassification")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class SecurityTokenXClassification
		extends WarehouseClassificationRelationshipTable<SecurityToken, Classification, SecurityTokenXClassification, SecurityTokenXClassificationQueryBuilder, Long, SecurityTokenXClassificationSecurityToken>
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
	protected SecurityTokenXClassificationSecurityToken configureDefaultsForNewToken(SecurityTokenXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "SecurityTokenXClassification(id=" + this.getId() + ", securityTokenID=" + this.getSecurityTokenID() + ", securities=" + this.getSecurities() + ")";
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof SecurityTokenXClassification))
		{
			return false;
		}
		final SecurityTokenXClassification other = (SecurityTokenXClassification) o;
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
		return other instanceof SecurityTokenXClassification;
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
