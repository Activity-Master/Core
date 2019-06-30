package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyXClassificationQueryBuilder;
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
@Table(name = "InvolvedPartyXClassification")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class InvolvedPartyXClassification
		extends WarehouseClassificationRelationshipTable<InvolvedParty, Classification, InvolvedPartyXClassification, InvolvedPartyXClassificationQueryBuilder, Long, InvolvedPartyXClassificationSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyXClassificationID")
	private Long id;
	@JoinColumn(name = "InvolvedPartyID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.EAGER)
	private InvolvedParty involvedPartyID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXClassificationSecurityToken> securities;

	public InvolvedPartyXClassification()
	{

	}

	public InvolvedPartyXClassification(Long involvedPartyXClassificationID)
	{
		this.id = involvedPartyXClassificationID;
	}

	@Override
	protected InvolvedPartyXClassificationSecurityToken configureDefaultsForNewToken(InvolvedPartyXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "InvolvedPartyXClassification(id=" + this.getId() + ", involvedPartyID=" + this.getInvolvedPartyID() + ", securities=" + this.getSecurities() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}

	public List<InvolvedPartyXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public InvolvedPartyXClassification setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyXClassification setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}

	public InvolvedPartyXClassification setSecurities(List<InvolvedPartyXClassificationSecurityToken> securities)
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
		if (!(o instanceof InvolvedPartyXClassification))
		{
			return false;
		}
		final InvolvedPartyXClassification other = (InvolvedPartyXClassification) o;
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
		return other instanceof InvolvedPartyXClassification;
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
