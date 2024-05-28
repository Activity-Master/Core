package com.guicedee.activitymaster.fsdm.db.entities.address;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.builders.AddressXGeographySecurityTokenQueryBuilder;
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
@Table(schema = "Address", name = "AddressXGeographySecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
public class AddressXGeographySecurityToken
		extends WarehouseSecurityTable<AddressXGeographySecurityToken, AddressXGeographySecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "AddressXGeographySecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "AddressXGeographyID",
	            referencedColumnName = "AddressXGeographyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private AddressXGeography base;
	
	public AddressXGeographySecurityToken()
	{
	
	}
	
	public AddressXGeographySecurityToken(java.lang.String addressXGeographySecurityTokenID)
	{
		this.id = addressXGeographySecurityTokenID;
	}
	
	public String toString()
	{
		return "AddressXGeographySecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public AddressXGeographySecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public AddressXGeographySecurityToken setBase(AddressXGeography base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public AddressXGeography getBase()
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
		AddressXGeographySecurityToken that = (AddressXGeographySecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
