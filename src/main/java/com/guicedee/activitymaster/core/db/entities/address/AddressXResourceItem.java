/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.address;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.address.builders.AddressXResourceItemQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.dto.IAddress;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IResourceItem;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Address",name = "AddressXResourceItem")
@XmlRootElement
@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class AddressXResourceItem
		extends WarehouseClassificationRelationshipTable<Address,
				                                                ResourceItem,
				                                                AddressXResourceItem,
				                                                AddressXResourceItemQueryBuilder,
				                                                java.util.UUID,
				                                                AddressXResourceItemSecurityToken,
				                                                IAddress<?>, IResourceItem<?>>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
			name = "AddressXResourceItemID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
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

	public AddressXResourceItem(UUID addressXResourceItemID)
	{
		this.id = addressXResourceItemID;
	}

	@Override
	protected AddressXResourceItemSecurityToken configureDefaultsForNewToken(AddressXResourceItemSecurityToken stAdmin, ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public java.util.UUID getId()
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

	public AddressXResourceItem setId(java.util.UUID id)
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
