/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.address;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.builders.AddressXResourceItemQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Address", name = "AddressXResourceItem")
@XmlRootElement
@Access(AccessType.FIELD)
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
		java.lang.String,
		AddressXResourceItemSecurityToken
		>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "AddressXResourceItemID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
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
	
	public AddressXResourceItem(java.lang.String addressXResourceItemID)
	{
		this.id = addressXResourceItemID;
	}
	
	@Override
	public void configureSecurityEntity(AddressXResourceItemSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	public AddressXResourceItem setId(java.lang.String id)
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
	public Address getPrimary()
	{
		return getAddressID();
	}
	
	@Override
	public ResourceItem getSecondary()
	{
		return getResourceItemID();
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public List<AddressXResourceItemSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public Address getAddressID()
	{
		return addressID;
	}
	
	public ResourceItem getResourceItemID()
	{
		return resourceItemID;
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
		return Objects.hashCode(getId());
	}
}
