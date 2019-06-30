package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyTypeQueryBuilder;
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
@Table(name = "InvolvedPartyXInvolvedPartyType")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class InvolvedPartyXInvolvedPartyType
		extends WarehouseRelationshipTable<InvolvedParty, InvolvedPartyType,
				                                  InvolvedPartyXInvolvedPartyType,
				                                  InvolvedPartyXInvolvedPartyTypeQueryBuilder, Long,
				                                  InvolvedPartyXInvolvedPartyTypeSecurityToken>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyXInvolvedPartyTypeID")
	private Long id;

	@JoinColumn(name = "InvolvedPartyID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedParty involvedPartyID;
	@JoinColumn(name = "InvolvedPartyTypeID",
			referencedColumnName = "InvolvedPartyTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedPartyType involvedPartyTypeID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyTypeSecurityToken> securities;

	public InvolvedPartyXInvolvedPartyType()
	{

	}

	public InvolvedPartyXInvolvedPartyType(Long involvedPartyXInvolvedPartyTypeID)
	{
		this.id = involvedPartyXInvolvedPartyTypeID;
	}

	@Override
	protected InvolvedPartyXInvolvedPartyTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyXInvolvedPartyTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "InvolvedPartyXInvolvedPartyType(id=" + this.getId() + ", involvedPartyID=" + this.getInvolvedPartyID() + ", involvedPartyTypeID=" + this.getInvolvedPartyTypeID() +
		       ", securities=" + this.getSecurities() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}

	public InvolvedPartyType getInvolvedPartyTypeID()
	{
		return this.involvedPartyTypeID;
	}

	public List<InvolvedPartyXInvolvedPartyTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public InvolvedPartyXInvolvedPartyType setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyXInvolvedPartyType setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}

	public InvolvedPartyXInvolvedPartyType setInvolvedPartyTypeID(InvolvedPartyType involvedPartyTypeID)
	{
		this.involvedPartyTypeID = involvedPartyTypeID;
		return this;
	}

	public InvolvedPartyXInvolvedPartyType setSecurities(List<InvolvedPartyXInvolvedPartyTypeSecurityToken> securities)
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
		if (!(o instanceof InvolvedPartyXInvolvedPartyType))
		{
			return false;
		}
		final InvolvedPartyXInvolvedPartyType other = (InvolvedPartyXInvolvedPartyType) o;
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
		return other instanceof InvolvedPartyXInvolvedPartyType;
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
