package com.guicedee.activitymaster.core.db.entities.product;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.product.builders.ProductXProductSecurityTokenQueryBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static javax.persistence.AccessType.*;

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
		extends WarehouseSecurityTable<ProductXProductSecurityToken, ProductXProductSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ProductXProductSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ProductXProductID",
			referencedColumnName = "ProductXProductID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ProductXProduct base;

	public ProductXProductSecurityToken()
	{

	}

	public ProductXProductSecurityToken(Long productXProductSecurityTokenID)
	{
		this.id = productXProductSecurityTokenID;
	}

	public String toString()
	{
		return "ProductXProductSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public ProductXProduct getBase()
	{
		return this.base;
	}

	public ProductXProductSecurityToken setId(Long id)
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
