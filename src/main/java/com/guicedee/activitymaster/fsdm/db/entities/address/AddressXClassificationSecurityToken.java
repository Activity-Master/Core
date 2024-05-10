package com.guicedee.activitymaster.fsdm.db.entities.address;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.builders.AddressXClassificationSecurityTokenQueryBuilder;
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
@Table(schema = "Address", name = "AddressXClassificationSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
@EqualsAndHashCode(of="id",callSuper = false)
public class AddressXClassificationSecurityToken
		extends IWarehouseSecurityTable<AddressXClassificationSecurityToken, AddressXClassificationSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "AddressXClassificationSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "AddressXClassificationID",
	            referencedColumnName = "AddressXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private AddressXClassification base;
	
	public AddressXClassificationSecurityToken()
	{
	
	}
	
	public AddressXClassificationSecurityToken(java.lang.String addressXClassificationSecurityTokenID)
	{
		this.id = addressXClassificationSecurityTokenID;
	}
	
	public String toString()
	{
		return "AddressXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public AddressXClassificationSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public AddressXClassificationSecurityToken setBase(AddressXClassification base)
	{
		this.base = base;
		return this;
	}
}
