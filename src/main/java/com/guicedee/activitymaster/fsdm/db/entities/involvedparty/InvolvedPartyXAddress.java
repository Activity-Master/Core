/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.Address;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXAddressQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Party",
       name = "InvolvedPartyXAddress")
@XmlRootElement

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class InvolvedPartyXAddress
		extends WarehouseClassificationRelationshipTable<InvolvedParty,
		Address,
		InvolvedPartyXAddress,
		InvolvedPartyXAddressQueryBuilder,
		java.lang.String,
		InvolvedPartyXAddressSecurityToken
		>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyXAddressID")
	@JsonValue
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "AddressID",
	            referencedColumnName = "AddressID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Address addressID;
	
	@JoinColumn(name = "InvolvedPartyID",
	            referencedColumnName = "InvolvedPartyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private InvolvedParty involvedPartyID;
	
@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<InvolvedPartyXAddressSecurityToken> securities;
	
	public InvolvedPartyXAddress()
	{
	
	}
	
	public InvolvedPartyXAddress(java.lang.String involvedPartyXAddressID)
	{
		this.id = involvedPartyXAddressID;
	}
	
	@Override
	public void configureSecurityEntity(InvolvedPartyXAddressSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public InvolvedPartyXAddress setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public Address getAddressID()
	{
		return this.addressID;
	}
	
	public InvolvedPartyXAddress setAddressID(Address addressID)
	{
		this.addressID = addressID;
		return this;
	}
	
	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}
	
	public InvolvedPartyXAddress setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}
	
	public List<InvolvedPartyXAddressSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public InvolvedPartyXAddress setSecurities(List<InvolvedPartyXAddressSecurityToken> securities)
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
		InvolvedPartyXAddress that = (InvolvedPartyXAddress) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public InvolvedParty getPrimary()
	{
		return getInvolvedPartyID();
	}
	
	@Override
	public Address getSecondary()
	{
		return getAddressID();
	}
}
