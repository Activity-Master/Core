package com.armineasy.activitymaster.activitymaster.db.entities.enterprise;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.builders.EnterpriseXClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "EnterpriseXClassification")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class EnterpriseXClassification
		extends WarehouseClassificationRelationshipTable<Enterprise, Classification, EnterpriseXClassification,
				                                                EnterpriseXClassificationQueryBuilder, Long, EnterpriseXClassificationSecurityToken>
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
		this.id = enterpriseXClassificationID;
	}

	@Override
	protected EnterpriseXClassificationSecurityToken configureDefaultsForNewToken(EnterpriseXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "EnterpriseXClassification(id=" + this.getId() + ", securities=" + this.getSecurities() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public List<EnterpriseXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}

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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof EnterpriseXClassification))
		{
			return false;
		}
		final EnterpriseXClassification other = (EnterpriseXClassification) o;
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
		return other instanceof EnterpriseXClassification;
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
