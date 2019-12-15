package com.guicedee.activitymaster.core.db.entities.enterprise;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.builders.EnterpriseXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

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
				                                                Long,
				                                                EnterpriseXClassificationSecurityToken,
				                                                IEnterprise<?>, IClassification<?>>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EnterpriseXClassificationID")
	private Long id;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<EnterpriseXClassificationSecurityToken> securities;

	public EnterpriseXClassification()
	{

	}

	public EnterpriseXClassification(Long enterpriseXClassificationID)
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
	public Long getId()
	{
		return id;
	}

	public List<EnterpriseXClassificationSecurityToken> getSecurities()
	{
		return securities;
	}

	@Override
	public EnterpriseXClassification setId(Long id)
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
