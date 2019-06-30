package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder;
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
@Table(name = "InvolvedPartyXInvolvedPartyIdentificationType")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class InvolvedPartyXInvolvedPartyIdentificationType
		extends WarehouseRelationshipTable<InvolvedParty, InvolvedPartyIdentificationType,
				                                  InvolvedPartyXInvolvedPartyIdentificationType,
				                                  InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder, Long,
				                                  InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyXInvolvedPartyIdentificationTypeID")
	private Long id;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> securities;

	@JoinColumn(name = "InvolvedPartyID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedParty involvedPartyID;

	@JoinColumn(name = "InvolvedPartyIdentificationTypeID",
			referencedColumnName = "InvolvedPartyIdentificationTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedPartyIdentificationType involvedPartyIdentificationTypeID;

	public InvolvedPartyXInvolvedPartyIdentificationType()
	{

	}

	public InvolvedPartyXInvolvedPartyIdentificationType(Long involvedPartyXInvolvedPartyIdentificationTypeID)
	{
		this.id = involvedPartyXInvolvedPartyIdentificationTypeID;
	}

	public InvolvedPartyXInvolvedPartyIdentificationType(Long involvedPartyXInvolvedPartyIdentificationTypeID, String value)
	{
		this.id = involvedPartyXInvolvedPartyIdentificationTypeID;
		setValue(value);
	}

	@Override
	protected InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "InvolvedPartyXInvolvedPartyIdentificationType(id=" + this.getId() + ", securities=" + this.getSecurities() + ", involvedPartyID=" + this.getInvolvedPartyID() +
		       ", involvedPartyIdentificationTypeID=" + this.getInvolvedPartyIdentificationTypeID() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}

	public InvolvedPartyIdentificationType getInvolvedPartyIdentificationTypeID()
	{
		return this.involvedPartyIdentificationTypeID;
	}

	public InvolvedPartyXInvolvedPartyIdentificationType setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyXInvolvedPartyIdentificationType setSecurities(List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public InvolvedPartyXInvolvedPartyIdentificationType setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}

	public InvolvedPartyXInvolvedPartyIdentificationType setInvolvedPartyIdentificationTypeID(InvolvedPartyIdentificationType involvedPartyIdentificationTypeID)
	{
		this.involvedPartyIdentificationTypeID = involvedPartyIdentificationTypeID;
		return this;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof InvolvedPartyXInvolvedPartyIdentificationType))
		{
			return false;
		}
		final InvolvedPartyXInvolvedPartyIdentificationType other = (InvolvedPartyXInvolvedPartyIdentificationType) o;
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
		return other instanceof InvolvedPartyXInvolvedPartyIdentificationType;
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
