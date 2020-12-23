package com.guicedee.activitymaster.core.db.entities.enterprise;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.builders.EnterpriseXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "EnterpriseXClassification")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
public class EnterpriseXClassification
		extends WarehouseClassificationRelationshipTable<Enterprise,
				                                                Classification,
				                                                EnterpriseXClassification,
				                                                EnterpriseXClassificationQueryBuilder,
				                                                java.util.UUID,
				                                                EnterpriseXClassificationSecurityToken,
				                                                IEnterprise<?>, IClassification<?>>
{

	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "EnterpriseXClassificationID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<EnterpriseXClassificationSecurityToken> securities;

	public EnterpriseXClassification()
	{

	}

	public EnterpriseXClassification(UUID enterpriseXClassificationID)
	{
		id = enterpriseXClassificationID;
	}

	@Override
	protected EnterpriseXClassificationSecurityToken configureDefaultsForNewToken(EnterpriseXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public java.util.UUID getId()
	{
		return id;
	}

	public List<EnterpriseXClassificationSecurityToken> getSecurities()
	{
		return securities;
	}

	@Override
	public EnterpriseXClassification setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public EnterpriseXClassification setSecurities(List<EnterpriseXClassificationSecurityToken> securities)
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
		EnterpriseXClassification that = (EnterpriseXClassification) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IEnterprise<?> getPrimary()
	{
		return getEnterpriseID();
	}

	@Override
	public IClassification<?> getSecondary()
	{
		return getClassificationID();
	}
}
