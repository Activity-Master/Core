package com.guicedee.activitymaster.fsdm.db.entities.address;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.builders.AddressXAddressQueryBuilder;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Address", name = "AddressXAddress")
@XmlRootElement
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressXAddress
		extends WarehouseClassificationRelationshipTable<Address,
                                Address,
                                AddressXAddress,
                                AddressXAddressQueryBuilder,
                                UUID,
                                AddressXAddressSecurityToken
                                >
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "AddressXAddressID")

	private java.util.UUID id;
	
	@JoinColumn(name = "AddressID",
	            referencedColumnName = "AddressID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Address addressID;
	@JoinColumn(name = "ComponentAddressID",
	            referencedColumnName = "AddressID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Address componentAddressID;
	
@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<AddressXAddressSecurityToken> securities;

	@Override
	public void configureSecurityEntity(AddressXAddressSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}

	
	public AddressXAddress setAddressID(Address addressID)
	{
		this.addressID = addressID;
		return this;
	}
	
	public AddressXAddress setComponentAddressID(Address componentAddressID)
	{
		this.componentAddressID = componentAddressID;
		return this;
	}
	
	public AddressXAddress setSecurities(List<AddressXAddressSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	@Override
	public Address getPrimary()
	{
		return getAddressID();
	}
	
	@Override
	public Address getSecondary()
	{
		return getComponentAddressID();
	}

	public Address getAddressID()
	{
		return addressID;
	}
	
	public Address getComponentAddressID()
	{
		return componentAddressID;
	}
	
	public List<AddressXAddressSecurityToken> getSecurities()
	{
		return securities;
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
		AddressXAddress that = (AddressXAddress) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}
