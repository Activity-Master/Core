/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.address;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.builders.AddressXClassificationQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Address", name = "AddressXClassification")
@XmlRootElement
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class AddressXClassification
		extends WarehouseClassificationRelationshipTable<Address,
		Classification,
		AddressXClassification,
		AddressXClassificationQueryBuilder,
		UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "AddressXClassificationID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
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
	
	public AddressXClassification(UUID addressXClassificationID)
	{
		this.id = addressXClassificationID;
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public AddressXClassification setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public Address getAddressID()
	{
		return this.addressID;
	}
	
	public AddressXClassification setAddressID(Address addressID)
	{
		this.addressID = addressID;
		return this;
	}
	
	public List<AddressXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
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
	public Address getPrimary()
	{
		return getAddressID();
	}
	
	@Override
	public Classification getSecondary()
	{
		return getClassificationID();
	}
}
