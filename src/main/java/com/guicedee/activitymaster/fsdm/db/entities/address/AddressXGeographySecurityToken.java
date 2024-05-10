package com.guicedee.activitymaster.fsdm.db.entities.address;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.builders.AddressXGeographySecurityTokenQueryBuilder;
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
@Table(schema = "Address", name = "AddressXGeographySecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
@EqualsAndHashCode(of = "id", callSuper = false)
public class AddressXGeographySecurityToken
		extends IWarehouseSecurityTable<AddressXGeographySecurityToken, AddressXGeographySecurityTokenQueryBuilder, String>
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
}
