package com.armineasy.activitymaster.activitymaster.db.entities.systems;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.builders.SystemsXClassificationQueryBuilder;
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
@Table(name = "SystemXClassification")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class SystemXClassification
		extends WarehouseClassificationRelationshipTable<Systems, Classification, SystemXClassification, SystemsXClassificationQueryBuilder,
				                                                Long, SystemXClassificationSecurityToken>
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

	public String toString()
	{
		return "SystemXClassification(id=" + this.getId() + ", securities=" + this.getSecurities() + ")";
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof SystemXClassification))
		{
			return false;
		}
		final SystemXClassification other = (SystemXClassification) o;
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
		return other instanceof SystemXClassification;
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
