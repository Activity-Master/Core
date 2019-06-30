package com.armineasy.activitymaster.activitymaster.db.entities.arrangement;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementXInvolvedPartyQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
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
@Table(name = "ArrangementXInvolvedParty")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ArrangementXInvolvedParty
		extends WarehouseClassificationRelationshipTable<Arrangement, InvolvedParty, ArrangementXInvolvedParty, ArrangementXInvolvedPartyQueryBuilder, Long, ArrangementXInvolvedPartySecurityToken>
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
	protected ArrangementXInvolvedPartySecurityToken configureDefaultsForNewToken(ArrangementXInvolvedPartySecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "ArrangementXInvolvedParty(id=" + this.getId() + ", arrangementID=" + this.getArrangementID() + ", involvedPartyID=" + this.getInvolvedPartyID() + ", securities=" +
		       this.getSecurities() + ")";
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof ArrangementXInvolvedParty))
		{
			return false;
		}
		final ArrangementXInvolvedParty other = (ArrangementXInvolvedParty) o;
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
		return other instanceof ArrangementXInvolvedParty;
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
