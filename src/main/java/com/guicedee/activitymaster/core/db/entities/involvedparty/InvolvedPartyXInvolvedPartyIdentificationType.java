package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IInvolvedParty;
import com.guicedee.activitymaster.core.services.dto.IInvolvedPartyIdentificationType;
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
@Table(schema="Party",name = "InvolvedPartyXInvolvedPartyIdentificationType")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyXInvolvedPartyIdentificationType
		extends WarehouseRelationshipTable<InvolvedParty, InvolvedPartyIdentificationType,
						                                  InvolvedPartyXInvolvedPartyIdentificationType,
				                                  InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder,
						                                  Long,
						                                  InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken,
				                                  IInvolvedParty<?>, IInvolvedPartyIdentificationType<?>
						                                  >
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
	protected InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
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
		InvolvedPartyXInvolvedPartyIdentificationType that = (InvolvedPartyXInvolvedPartyIdentificationType) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IInvolvedParty<?> getPrimary()
	{
		return getInvolvedPartyID();
	}

	@Override
	public IInvolvedPartyIdentificationType<?> getSecondary()
	{
		return getInvolvedPartyIdentificationTypeID();
	}
}
