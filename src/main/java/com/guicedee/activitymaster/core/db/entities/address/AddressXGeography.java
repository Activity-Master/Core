package com.guicedee.activitymaster.core.db.entities.address;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.address.builders.AddressXGeographyQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;
import com.guicedee.activitymaster.core.services.dto.IAddress;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IGeography;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Address",name = "AddressXGeography")
@XmlRootElement

@Access(FIELD)
public class AddressXGeography
		extends WarehouseClassificationRelationshipTable<Address,
				                                                Geography,
				                                                AddressXGeography,
				                                                AddressXGeographyQueryBuilder,
				                                                java.util.UUID,
				                                                AddressXGeographySecurityToken,
				                                                IAddress<?>, IGeography<?>>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
			name = "AddressXGeographyID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

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

	public AddressXGeography(UUID addressXGeographyID)
	{
		this.id = addressXGeographyID;
	}

	@Override
	protected AddressXGeographySecurityToken configureDefaultsForNewToken(AddressXGeographySecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public java.util.UUID getId()
	{
		return this.id;
	}

	public Address getAddressID()
	{
		return this.addressID;
	}

	public Geography getGeographyID()
	{
		return this.geographyID;
	}

	public List<AddressXGeographySecurityToken> getSecurities()
	{
		return this.securities;
	}

	public AddressXGeography setId(java.util.UUID id)
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
		AddressXGeography that = (AddressXGeography) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IAddress<?> getPrimary()
	{
		return getAddressID();
	}

	@Override
	public IGeography<?> getSecondary()
	{
		return getGeographyID();
	}
}
