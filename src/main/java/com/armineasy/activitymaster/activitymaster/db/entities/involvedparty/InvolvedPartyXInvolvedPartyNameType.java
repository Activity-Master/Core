/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyNameTypeQueryBuilder;
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
@Table(name = "InvolvedPartyXInvolvedPartyNameType")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class InvolvedPartyXInvolvedPartyNameType
		extends WarehouseRelationshipTable<InvolvedParty, InvolvedPartyNameType,
				                                  InvolvedPartyXInvolvedPartyNameType, InvolvedPartyXInvolvedPartyNameTypeQueryBuilder, Long, InvolvedPartyXInvolvedPartyNameTypeSecurityToken>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyXInvolvedPartyNameTypeID")
	private Long id;

	@JoinColumn(name = "InvolvedPartyID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedParty involvedPartyID;
	@JoinColumn(name = "InvolvedPartyNameTypeID",
			referencedColumnName = "InvolvedPartyNameTypeID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedPartyNameType involvedPartyNameTypeID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> securities;

	public InvolvedPartyXInvolvedPartyNameType()
	{

	}

	public InvolvedPartyXInvolvedPartyNameType(Long involvedPartyXInvolvedPartyNameTypeID)
	{
		this.id = involvedPartyXInvolvedPartyNameTypeID;
	}

	public InvolvedPartyXInvolvedPartyNameType(Long involvedPartyXInvolvedPartyNameTypeID, String involvedPartyName)
	{
		this.id = involvedPartyXInvolvedPartyNameTypeID;
		setValue(involvedPartyName);
	}

	@Override
	protected InvolvedPartyXInvolvedPartyNameTypeSecurityToken configureDefaultsForNewToken(InvolvedPartyXInvolvedPartyNameTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "InvolvedPartyXInvolvedPartyNameType(id=" + this.getId() + ", involvedPartyID=" + this.getInvolvedPartyID() + ", involvedPartyNameTypeID=" +
		       this.getInvolvedPartyNameTypeID() + ", securities=" + this.getSecurities() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}

	public InvolvedPartyNameType getInvolvedPartyNameTypeID()
	{
		return this.involvedPartyNameTypeID;
	}

	public List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public InvolvedPartyXInvolvedPartyNameType setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyXInvolvedPartyNameType setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}

	public InvolvedPartyXInvolvedPartyNameType setInvolvedPartyNameTypeID(InvolvedPartyNameType involvedPartyNameTypeID)
	{
		this.involvedPartyNameTypeID = involvedPartyNameTypeID;
		return this;
	}

	public InvolvedPartyXInvolvedPartyNameType setSecurities(List<InvolvedPartyXInvolvedPartyNameTypeSecurityToken> securities)
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
		if (!(o instanceof InvolvedPartyXInvolvedPartyNameType))
		{
			return false;
		}
		final InvolvedPartyXInvolvedPartyNameType other = (InvolvedPartyXInvolvedPartyNameType) o;
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
		return other instanceof InvolvedPartyXInvolvedPartyNameType;
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
