package com.armineasy.activitymaster.activitymaster.db.entities.activeflag;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.builders.ActiveFlagXClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
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
@Table(name = "ActiveFlagXClassification")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ActiveFlagXClassification
		extends WarehouseClassificationRelationshipTable<ActiveFlag, Classification, ActiveFlagXClassification, ActiveFlagXClassificationQueryBuilder, Long, ActiveFlagXClassificationSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ActiveFlagXClassificationID")
	private Long id;
	@JoinColumn(name = "SystemID",
			referencedColumnName = "SystemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Systems systemID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ActiveFlagXClassificationSecurityToken> securities;

	public ActiveFlagXClassification()
	{

	}

	public ActiveFlagXClassification(Long activeFlagXClassificationID)
	{
		this.id = activeFlagXClassificationID;
	}

	@Override
	protected ActiveFlagXClassificationSecurityToken configureDefaultsForNewToken(ActiveFlagXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "ActiveFlagXClassification(id=" + this.getId() + ", systemID=" + this.getSystemID() + ", securities=" + this.getSecurities() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public Systems getSystemID()
	{
		return this.systemID;
	}

	public List<ActiveFlagXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public ActiveFlagXClassification setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ActiveFlagXClassification setSystemID(Systems systemID)
	{
		this.systemID = systemID;
		return this;
	}

	public ActiveFlagXClassification setSecurities(List<ActiveFlagXClassificationSecurityToken> securities)
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
		if (!(o instanceof ActiveFlagXClassification))
		{
			return false;
		}
		final ActiveFlagXClassification other = (ActiveFlagXClassification) o;
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
		return other instanceof ActiveFlagXClassification;
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
