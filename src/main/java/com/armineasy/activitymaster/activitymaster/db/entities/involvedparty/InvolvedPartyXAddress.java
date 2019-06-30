/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyXAddressQueryBuilder;
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
@Table(name = "InvolvedPartyXAddress")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class InvolvedPartyXAddress
		extends WarehouseClassificationRelationshipTable<InvolvedParty, Address, InvolvedPartyXAddress, InvolvedPartyXAddressQueryBuilder, Long, InvolvedPartyXAddressSecurityToken>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyXAddressID")
	private Long id;

	@JoinColumn(name = "AddressID",
			referencedColumnName = "AddressID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Address addressID;

	@JoinColumn(name = "InvolvedPartyID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedParty involvedPartyID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXAddressSecurityToken> securities;

	public InvolvedPartyXAddress()
	{

	}

	public InvolvedPartyXAddress(Long involvedPartyXAddressID)
	{
		this.id = involvedPartyXAddressID;
	}

	@Override
	protected InvolvedPartyXAddressSecurityToken configureDefaultsForNewToken(InvolvedPartyXAddressSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "InvolvedPartyXAddress(id=" + this.getId() + ", addressID=" + this.getAddressID() + ", involvedPartyID=" + this.getInvolvedPartyID() + ", securities=" +
		       this.getSecurities() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public Address getAddressID()
	{
		return this.addressID;
	}

	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}

	public List<InvolvedPartyXAddressSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public InvolvedPartyXAddress setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyXAddress setAddressID(Address addressID)
	{
		this.addressID = addressID;
		return this;
	}

	public InvolvedPartyXAddress setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}

	public InvolvedPartyXAddress setSecurities(List<InvolvedPartyXAddressSecurityToken> securities)
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
		if (!(o instanceof InvolvedPartyXAddress))
		{
			return false;
		}
		final InvolvedPartyXAddress other = (InvolvedPartyXAddress) o;
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
		return other instanceof InvolvedPartyXAddress;
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
