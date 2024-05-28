/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.address;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.builders.AddressXResourceItemSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Address", name = "AddressXResourceItemSecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
public class AddressXResourceItemSecurityToken
		extends WarehouseSecurityTable<AddressXResourceItemSecurityToken, AddressXResourceItemSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "AddressXResourceItemSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "AddressXResourceItemID",
	            referencedColumnName = "AddressXResourceItemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private AddressXResourceItem base;
	
	public AddressXResourceItemSecurityToken()
	{
	
	}
	
	public AddressXResourceItemSecurityToken(java.lang.String addressXResourceItemSecurityTokenID)
	{
		this.id = addressXResourceItemSecurityTokenID;
	}
	
	public String toString()
	{
		return "AddressXResourceItemSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public AddressXResourceItemSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public AddressXResourceItemSecurityToken setBase(AddressXResourceItem base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public AddressXResourceItem getBase()
	{
		return base;
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
		AddressXResourceItemSecurityToken that = (AddressXResourceItemSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
