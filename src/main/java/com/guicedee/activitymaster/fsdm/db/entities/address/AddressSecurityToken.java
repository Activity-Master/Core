package com.guicedee.activitymaster.fsdm.db.entities.address;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.builders.AddressSecurityTokenQueryBuilder;
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
@Table(schema = "Address", name = "AddressSecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
public class AddressSecurityToken
		extends WarehouseSecurityTable<AddressSecurityToken, AddressSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "AddressSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "AddressID",
	            referencedColumnName = "AddressID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Address base;
	
	public AddressSecurityToken()
	{
	
	}
	
	public AddressSecurityToken(java.lang.String addressSecurityTokenID)
	{
		this.id = addressSecurityTokenID;
	}
	
	public String toString()
	{
		return "AddressSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public AddressSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public AddressSecurityToken setBase(Address base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public Address getBase()
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
		AddressSecurityToken that = (AddressSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
