package com.guicedee.activitymaster.fsdm.db.entities.address;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.builders.AddressSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;


/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Getter
@Entity
@Table(schema = "Address", name = "AddressSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
@EqualsAndHashCode(of = "id", callSuper = false)
public class AddressSecurityToken
		extends IWarehouseSecurityTable<AddressSecurityToken, AddressSecurityTokenQueryBuilder, String>
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
}
