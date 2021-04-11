package com.guicedee.activitymaster.fsdm.db.entities.product;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.product.builders.ProductXProductSecurityTokenQueryBuilder;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Product",name = "ProductXProductSecurityToken")
@XmlRootElement

@Access(FIELD)
public class ProductXProductSecurityToken
		extends WarehouseSecurityTable<ProductXProductSecurityToken, ProductXProductSecurityTokenQueryBuilder, UUID>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "ProductXProductSecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;

	@JoinColumn(name = "ProductXProductID",
			referencedColumnName = "ProductXProductID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ProductXProduct base;

	public ProductXProductSecurityToken()
	{

	}

	public ProductXProductSecurityToken(UUID productXProductSecurityTokenID)
	{
		this.id = productXProductSecurityTokenID;
	}

	public String toString()
	{
		return "ProductXProductSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public UUID getId()
	{
		return this.id;
	}

	public ProductXProduct getBase()
	{
		return this.base;
	}

	public ProductXProductSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}

	public ProductXProductSecurityToken setBase(ProductXProduct base)
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
		if (!(o instanceof ProductXProductSecurityToken))
		{
			return false;
		}
		final ProductXProductSecurityToken other = (ProductXProductSecurityToken) o;
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
		return other instanceof ProductXProductSecurityToken;
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
