package com.guicedee.activitymaster.fsdm.db.entities.address;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.builders.AddressXGeographyQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.geography.Geography;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Getter
@Entity
@Table(schema = "Address", name = "AddressXGeography")
@XmlRootElement
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@EqualsAndHashCode(of = "id", callSuper = false)
public class AddressXGeography
		extends WarehouseClassificationRelationshipTable<Address,
		Geography,
		AddressXGeography,
		AddressXGeographyQueryBuilder,
		java.lang.String,
		AddressXGeographySecurityToken
		>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "AddressXGeographyID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "AddressID",
	            referencedColumnName = "AddressID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Address addressID;
	@JoinColumn(name = "GeographyID",
	            referencedColumnName = "GeographyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Geography geographyID;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<AddressXGeographySecurityToken> securities;
	
	public AddressXGeography()
	{
	
	}
	
	public AddressXGeography(java.lang.String addressXGeographyID)
	{
		this.id = addressXGeographyID;
	}
	
	public AddressXGeography setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public AddressXGeography setAddressID(Address addressID)
	{
		this.addressID = addressID;
		return this;
	}
	
	public AddressXGeography setGeographyID(Geography geographyID)
	{
		this.geographyID = geographyID;
		return this;
	}
	
	public AddressXGeography setSecurities(List<AddressXGeographySecurityToken> securities)
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
	public Geography getSecondary()
	{
		return getGeographyID();
	}
}
