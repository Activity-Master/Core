/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.address;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.address.builders.AddressXResourceItemQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.dto.IAddress;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IResourceItem;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;


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
@Table(name = "AddressXResourceItem")
@XmlRootElement

@Access(FIELD)
public class AddressXResourceItem
		extends WarehouseClassificationRelationshipTable<Address,
				                                                ResourceItem,
				                                                AddressXResourceItem,
				                                                AddressXResourceItemQueryBuilder,
				                                                Long,
				                                                AddressXResourceItemSecurityToken,
				                                                IAddress<?>, IResourceItem<?>>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "AddressXResourceItemID")
	private Long id;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<AddressXResourceItemSecurityToken> securities;

	@JoinColumn(name = "AddressID",
			referencedColumnName = "AddressID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Address addressID;

	@JoinColumn(name = "ResourceItemID",
			referencedColumnName = "ResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ResourceItem resourceItemID;

	public AddressXResourceItem()
	{

	}

	public AddressXResourceItem(Long addressXResourceItemID)
	{
		this.id = addressXResourceItemID;
	}

	@Override
	protected AddressXResourceItemSecurityToken configureDefaultsForNewToken(AddressXResourceItemSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
	{
		return this.id;
	}

	public List<AddressXResourceItemSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public Address getAddressID()
	{
		return this.addressID;
	}

	public ResourceItem getResourceItemID()
	{
		return this.resourceItemID;
	}

	public AddressXResourceItem setId(Long id)
	{
		this.id = id;
		return this;
	}

	public AddressXResourceItem setSecurities(List<AddressXResourceItemSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public AddressXResourceItem setAddressID(Address addressID)
	{
		this.addressID = addressID;
		return this;
	}

	public AddressXResourceItem setResourceItemID(ResourceItem resourceItemID)
	{
		this.resourceItemID = resourceItemID;
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
		AddressXResourceItem that = (AddressXResourceItem) o;
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
	public IResourceItem<?> getSecondary()
	{
		return getResourceItemID();
	}
}
