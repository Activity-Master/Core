package com.guicedee.activitymaster.core.db.entities.product;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.product.builders.ProductXProductTypeSecurityTokenQueryBuilder;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Product",
       name = "ProductXProductTypeSecurityToken")
@XmlRootElement

@Access(FIELD)
public class ProductXProductTypeSecurityToken
		extends WarehouseSecurityTable<ProductXProductTypeSecurityToken, ProductXProductTypeSecurityTokenQueryBuilder, java.util.UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
	        name = "ProductXProductTypeSecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
	@JoinColumn(name = "ProductXProductTypeID",
	            referencedColumnName = "ProductXProductTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ProductXProductType base;
	
	public ProductXProductTypeSecurityToken()
	{
	
	}
	
	public ProductXProductTypeSecurityToken(UUID productXProductTypeSecurityTokenID)
	{
		this.id = productXProductTypeSecurityTokenID;
	}
	
	@Override
	public String toString()
	{
		return "ProductXProductTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	@Override
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	public ProductXProductType getBase()
	{
		return this.base;
	}
	
	@Override
	public ProductXProductTypeSecurityToken setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public ProductXProductTypeSecurityToken setBase(ProductXProductType base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof ProductXProductTypeSecurityToken))
		{
			return false;
		}
		final ProductXProductTypeSecurityToken other = (ProductXProductTypeSecurityToken) o;
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
		return other instanceof ProductXProductTypeSecurityToken;
	}
	
	@Override
	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}
