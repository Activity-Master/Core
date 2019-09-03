package com.armineasy.activitymaster.activitymaster.db.entities.address;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.address.builders.AddressXGeographyQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.Geography;
import com.armineasy.activitymaster.activitymaster.services.dto.IAddress;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IGeography;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "AddressXGeography")
@XmlRootElement

@Access(FIELD)
public class AddressXGeography
		extends WarehouseClassificationRelationshipTable<Address,
				                                                Geography,
				                                                AddressXGeography,
				                                                AddressXGeographyQueryBuilder,
				                                                Long,
				                                                AddressXGeographySecurityToken,
				                                                IAddress<?>, IGeography<?>>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "AddressXGeographyID")
	private Long id;

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

	public AddressXGeography(Long addressXGeographyID)
	{
		this.id = addressXGeographyID;
	}

	@Override
	protected AddressXGeographySecurityToken configureDefaultsForNewToken(AddressXGeographySecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
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

	public AddressXGeography setId(Long id)
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
