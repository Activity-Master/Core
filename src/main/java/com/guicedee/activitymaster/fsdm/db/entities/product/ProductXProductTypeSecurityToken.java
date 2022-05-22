package com.guicedee.activitymaster.fsdm.db.entities.product;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.product.builders.ProductXProductTypeSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.UUID;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Product",
       name = "ProductXProductTypeSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class ProductXProductTypeSecurityToken
		extends WarehouseSecurityTable<ProductXProductTypeSecurityToken, ProductXProductTypeSecurityTokenQueryBuilder, UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ProductXProductTypeSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
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
	public UUID getId()
	{
		return this.id;
	}
	
	@Override
	public ProductXProductTypeSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public ProductXProductType getBase()
	{
		return this.base;
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
		if (!other.canEqual(this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		return this$id == null ? other$id == null : this$id.equals(other$id);
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
