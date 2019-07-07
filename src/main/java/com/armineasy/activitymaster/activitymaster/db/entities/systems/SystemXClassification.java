package com.armineasy.activitymaster.activitymaster.db.entities.systems;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.builders.SystemsXClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "SystemXClassification")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class SystemXClassification
		extends WarehouseClassificationRelationshipTable<Systems,
				                                                Classification,
				                                                SystemXClassification,
				                                                SystemsXClassificationQueryBuilder,
				                                                Long,
				                                                SystemXClassificationSecurityToken,
				                                                ISystems<?>, IClassification<?>>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "SystemXClassificationID")
	private Long id;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<SystemXClassificationSecurityToken> securities;

	public SystemXClassification()
	{

	}

	public SystemXClassification(Long systemXClassificationID)
	{
		this.id = systemXClassificationID;
	}

	@Override
	protected SystemXClassificationSecurityToken configureDefaultsForNewToken(SystemXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
	{
		return this.id;
	}

	public List<SystemXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public SystemXClassification setId(Long id)
	{
		this.id = id;
		return this;
	}

	public SystemXClassification setSecurities(List<SystemXClassificationSecurityToken> securities)
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
		SystemXClassification that = (SystemXClassification) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public ISystems<?> getPrimary()
	{
		return getSystemID();
	}

	@Override
	public IClassification<?> getSecondary()
	{
		return getClassificationID();
	}
}
