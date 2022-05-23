package com.guicedee.activitymaster.fsdm.db.entities.product;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.product.builders.ProductTypeXClassificationSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;


/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Product", name = "ProductTypeXClassificationSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class ProductTypeXClassificationSecurityToken
		extends WarehouseSecurityTable<ProductTypeXClassificationSecurityToken, ProductTypeXClassificationSecurityTokenQueryBuilder, java.lang.String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ProductTypeXClassificationSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ProductTypeXClassificationID",
	            referencedColumnName = "ProductTypeXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ProductTypeXClassification base;
	
	public ProductTypeXClassificationSecurityToken()
	{
	
	}
	
	public ProductTypeXClassificationSecurityToken(java.lang.String productXClassificationSecurityTokenID)
	{
		this.id = productXClassificationSecurityTokenID;
	}
	
	public String toString()
	{
		return "ProductTypeXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public ProductTypeXClassificationSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ProductTypeXClassification getBase()
	{
		return this.base;
	}
	
	public ProductTypeXClassificationSecurityToken setBase(ProductTypeXClassification base)
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
		if (!(o instanceof ProductTypeXClassificationSecurityToken))
		{
			return false;
		}
		final ProductTypeXClassificationSecurityToken other = (ProductTypeXClassificationSecurityToken) o;
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
		return other instanceof ProductTypeXClassificationSecurityToken;
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
