package com.guicedee.activitymaster.core.db.entities.arrangement;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementXInvolvedPartyQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.services.dto.IArrangement;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IInvolvedParty;
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
@Table(schema="Arrangement",name = "ArrangementXInvolvedParty")
@XmlRootElement

@Access(FIELD)
public class ArrangementXInvolvedParty
		extends WarehouseClassificationRelationshipTable<Arrangement,
				                                                InvolvedParty,
						                                                ArrangementXInvolvedParty,
				                                                ArrangementXInvolvedPartyQueryBuilder,
						                                                Long,
						                                                ArrangementXInvolvedPartySecurityToken,
				                                                IArrangement<?>, IInvolvedParty<?>>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ArrangementXInvolvedPartyID")
	private Long id;

	@JoinColumn(name = "ArrangementID",
			referencedColumnName = "ArrangementID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Arrangement arrangementID;

	@JoinColumn(name = "InvolvedPartyID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedParty involvedPartyID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementXInvolvedPartySecurityToken> securities;

	public ArrangementXInvolvedParty()
	{

	}

	public ArrangementXInvolvedParty(Long arrangementXInvolvedPartyID)
	{
		this.id = arrangementXInvolvedPartyID;
	}

	@Override
	protected ArrangementXInvolvedPartySecurityToken configureDefaultsForNewToken(ArrangementXInvolvedPartySecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
	{
		return this.id;
	}

	public Arrangement getArrangementID()
	{
		return this.arrangementID;
	}

	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}

	public List<ArrangementXInvolvedPartySecurityToken> getSecurities()
	{
		return this.securities;
	}

	public ArrangementXInvolvedParty setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ArrangementXInvolvedParty setArrangementID(Arrangement arrangementID)
	{
		this.arrangementID = arrangementID;
		return this;
	}

	public ArrangementXInvolvedParty setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}

	public ArrangementXInvolvedParty setSecurities(List<ArrangementXInvolvedPartySecurityToken> securities)
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
		ArrangementXInvolvedParty that = (ArrangementXInvolvedParty) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IArrangement<?> getPrimary()
	{
		return getArrangementID();
	}

	@Override
	public IInvolvedParty<?> getSecondary()
	{
		return getInvolvedPartyID();
	}
}
