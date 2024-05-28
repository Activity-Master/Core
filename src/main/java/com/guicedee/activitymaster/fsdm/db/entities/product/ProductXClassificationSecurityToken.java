package com.guicedee.activitymaster.fsdm.db.entities.product;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.product.builders.ProductXClassificationSecurityTokenQueryBuilder;
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
@Table(schema = "Product", name = "ProductXClassificationSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
public class ProductXClassificationSecurityToken
		extends WarehouseSecurityTable<ProductXClassificationSecurityToken, ProductXClassificationSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ProductXClassificationSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ProductXClassificationID",
	            referencedColumnName = "ProductXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ProductXClassification base;
	
	public ProductXClassificationSecurityToken()
	{
	
	}
	
	public ProductXClassificationSecurityToken(java.lang.String productXClassificationSecurityTokenID)
	{
		this.id = productXClassificationSecurityTokenID;
	}
	
	public String toString()
	{
		return "ProductXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public ProductXClassificationSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ProductXClassification getBase()
	{
		return this.base;
	}
	
	public ProductXClassificationSecurityToken setBase(ProductXClassification base)
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
		if (!(o instanceof ProductXClassificationSecurityToken))
		{
			return false;
		}
		final ProductXClassificationSecurityToken other = (ProductXClassificationSecurityToken) o;
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
		return other instanceof ProductXClassificationSecurityToken;
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
