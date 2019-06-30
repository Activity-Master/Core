package com.armineasy.activitymaster.activitymaster.db.entities.address;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.address.builders.AddressXGeographyQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.Geography;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "AddressXGeography")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class AddressXGeography
		extends WarehouseClassificationRelationshipTable<Address, Geography, AddressXGeography, AddressXGeographyQueryBuilder, Long, AddressXGeographySecurityToken>
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

	public String toString()
	{
		return "AddressXGeography(id=" + this.getId() + ", addressID=" + this.getAddressID() + ", geographyID=" + this.getGeographyID() + ", securities=" + this.getSecurities() +
		       ")";
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof AddressXGeography))
		{
			return false;
		}
		final AddressXGeography other = (AddressXGeography) o;
		if (!other.canEqual((Object) this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
		{
			return false;
		}
		return true;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof AddressXGeography;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}
