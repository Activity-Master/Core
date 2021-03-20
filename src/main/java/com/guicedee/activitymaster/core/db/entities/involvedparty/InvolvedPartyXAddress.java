/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyXAddressQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Party",
       name = "InvolvedPartyXAddress")
@XmlRootElement

@Access(FIELD)
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
		java.util.UUID>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "InvolvedPartyXAddressID")
	@JsonValue
	@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
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
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXAddressSecurityToken> securities;
	
	public InvolvedPartyXAddress()
	{
	
	}
	
	public InvolvedPartyXAddress(UUID involvedPartyXAddressID)
	{
		this.id = involvedPartyXAddressID;
	}
	
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	public Address getAddressID()
	{
		return this.addressID;
	}
	
	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}
	
	public List<InvolvedPartyXAddressSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public InvolvedPartyXAddress setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public InvolvedPartyXAddress setAddressID(Address addressID)
	{
		this.addressID = addressID;
		return this;
	}
	
	public InvolvedPartyXAddress setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
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
