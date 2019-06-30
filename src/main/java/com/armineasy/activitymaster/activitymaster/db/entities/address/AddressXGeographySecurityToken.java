package com.armineasy.activitymaster.activitymaster.db.entities.address;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.address.builders.AddressXGeographySecurityTokenQueryBuilder;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "AddressXGeographySecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class AddressXGeographySecurityToken
		extends WarehouseSecurityTable<AddressXGeographySecurityToken, AddressXGeographySecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "AddressXGeographySecurityTokenID")
	private Long id;

	@JoinColumn(name = "AddressXGeographyID",
			referencedColumnName = "AddressXGeographyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private AddressXGeography base;

	public AddressXGeographySecurityToken()
	{

	}

	public AddressXGeographySecurityToken(Long addressXGeographySecurityTokenID)
	{
		this.id = addressXGeographySecurityTokenID;
	}

	public String toString()
	{
		return "AddressXGeographySecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public AddressXGeography getBase()
	{
		return this.base;
	}

	public AddressXGeographySecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public AddressXGeographySecurityToken setBase(AddressXGeography base)
	{
		this.base = base;
		return this;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof AddressXGeographySecurityToken))
		{
			return false;
		}
		final AddressXGeographySecurityToken other = (AddressXGeographySecurityToken) o;
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
		return other instanceof AddressXGeographySecurityToken;
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
