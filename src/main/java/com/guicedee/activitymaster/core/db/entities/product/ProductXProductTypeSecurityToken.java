package com.guicedee.activitymaster.core.db.entities.product;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.product.builders.ProductXProductTypeSecurityTokenQueryBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.persistence.AccessType.*;

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
		extends WarehouseSecurityTable<ProductXProductTypeSecurityToken, ProductXProductTypeSecurityTokenQueryBuilder, Long>
{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
	        name = "ProductXProductTypeSecurityTokenID")
	private Long id;
	
	@JoinColumn(name = "ProductXProductTypeID",
	            referencedColumnName = "ProductXProductTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ProductXProductType base;
	
	public ProductXProductTypeSecurityToken()
	{
	
	}
	
	public ProductXProductTypeSecurityToken(Long productXProductTypeSecurityTokenID)
	{
		this.id = productXProductTypeSecurityTokenID;
	}
	
	@Override
	public String toString()
	{
		return "ProductXProductTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	@Override
	public Long getId()
	{
		return this.id;
	}
	
	public ProductXProductType getBase()
	{
		return this.base;
	}
	
	@Override
	public ProductXProductTypeSecurityToken setId(Long id)
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
