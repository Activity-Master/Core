/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.address;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.address.builders.AddressXClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.services.dto.IAddress;
import com.armineasy.activitymaster.activitymaster.services.dto.IClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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
		extends WarehouseClassificationRelationshipTable<Address,
				                                                Classification,
				                                                AddressXClassification,
				                                                AddressXClassificationQueryBuilder,
				                                                Long,
				                                                AddressXClassificationSecurityToken,
				                                                IAddress<?>, IClassification<?>>
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
		AddressXClassification that = (AddressXClassification) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IAddress<?> getPrimary()
	{
		return getAddressID();
	}

	@Override
	public IClassification<?> getSecondary()
	{
		return getClassificationID();
	}
}
