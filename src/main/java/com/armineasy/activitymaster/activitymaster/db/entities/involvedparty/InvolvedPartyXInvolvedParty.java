/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyQueryBuilder;
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
@Table(name = "InvolvedPartyXInvolvedParty")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class InvolvedPartyXInvolvedParty
		extends WarehouseClassificationRelationshipTable<InvolvedParty, InvolvedParty, InvolvedPartyXInvolvedParty, InvolvedPartyXInvolvedPartyQueryBuilder, Long, InvolvedPartyXInvolvedPartySecurityToken>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyXInvolvedPartyID")
	private Long id;

	@JoinColumn(name = "ChildInvolvedPartyID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedParty childInvolvedPartyID;
	@JoinColumn(name = "ParentInvolvedPartyID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedParty parentInvolvedPartyID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartySecurityToken> securities;

	public InvolvedPartyXInvolvedParty()
	{

	}

	public InvolvedPartyXInvolvedParty(Long involvedPartyXInvolvedPartyID)
	{
		this.id = involvedPartyXInvolvedPartyID;
	}

	@Override
	protected InvolvedPartyXInvolvedPartySecurityToken configureDefaultsForNewToken(InvolvedPartyXInvolvedPartySecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "InvolvedPartyXInvolvedParty(id=" + this.getId() + ", childInvolvedPartyID=" + this.getChildInvolvedPartyID() + ", parentInvolvedPartyID=" +
		       this.getParentInvolvedPartyID() + ", securities=" + this.getSecurities() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public InvolvedParty getChildInvolvedPartyID()
	{
		return this.childInvolvedPartyID;
	}

	public InvolvedParty getParentInvolvedPartyID()
	{
		return this.parentInvolvedPartyID;
	}

	public List<InvolvedPartyXInvolvedPartySecurityToken> getSecurities()
	{
		return this.securities;
	}

	public InvolvedPartyXInvolvedParty setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyXInvolvedParty setChildInvolvedPartyID(InvolvedParty childInvolvedPartyID)
	{
		this.childInvolvedPartyID = childInvolvedPartyID;
		return this;
	}

	public InvolvedPartyXInvolvedParty setParentInvolvedPartyID(InvolvedParty parentInvolvedPartyID)
	{
		this.parentInvolvedPartyID = parentInvolvedPartyID;
		return this;
	}

	public InvolvedPartyXInvolvedParty setSecurities(List<InvolvedPartyXInvolvedPartySecurityToken> securities)
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
		if (!(o instanceof InvolvedPartyXInvolvedParty))
		{
			return false;
		}
		final InvolvedPartyXInvolvedParty other = (InvolvedPartyXInvolvedParty) o;
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
		return other instanceof InvolvedPartyXInvolvedParty;
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
