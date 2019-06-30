/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.address;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.address.builders.AddressXClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
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
@Table(name = "AddressXClassification")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class AddressXClassification
		extends WarehouseClassificationRelationshipTable<Address, Classification, AddressXClassification, AddressXClassificationQueryBuilder, Long, AddressXClassificationSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "AddressXClassificationID")
	private Long id;

	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	@JoinColumn(name = "AddressID",
			referencedColumnName = "AddressID",
			nullable = false)
	private Address addressID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<AddressXClassificationSecurityToken> securities;

	public AddressXClassification()
	{

	}

	public AddressXClassification(Long addressXClassificationID)
	{
		this.id = addressXClassificationID;
	}

	@Override
	protected AddressXClassificationSecurityToken configureDefaultsForNewToken(AddressXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "AddressXClassification(id=" + this.getId() + ", addressID=" + this.getAddressID() + ", securities=" + this.getSecurities() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public Address getAddressID()
	{
		return this.addressID;
	}

	public List<AddressXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public AddressXClassification setId(Long id)
	{
		this.id = id;
		return this;
	}

	public AddressXClassification setAddressID(Address addressID)
	{
		this.addressID = addressID;
		return this;
	}

	public AddressXClassification setSecurities(List<AddressXClassificationSecurityToken> securities)
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
		if (!(o instanceof AddressXClassification))
		{
			return false;
		}
		final AddressXClassification other = (AddressXClassification) o;
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
		return other instanceof AddressXClassification;
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
