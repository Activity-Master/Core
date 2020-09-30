package com.guicedee.activitymaster.core.db.entities.product;


import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.core.db.entities.product.builders.ProductXProductTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IProduct;
import com.guicedee.activitymaster.core.services.dto.IProductType;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IProductTypeValue;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Product",
       name = "ProductXProductType")
@XmlRootElement
@Access(FIELD)
public class ProductXProductType
		extends WarehouseClassificationRelationshipTypesTable<Product,
				ProductType,
				ProductXProductType,
				ProductXProductTypeQueryBuilder,
				IProductTypeValue<?>,
				Long,
				ProductXProductTypeSecurityToken,
				IProduct<?>, IProductType<?>>
{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
	        name = "ProductXProductTypeID")
	private Long id;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ProductXProductTypeSecurityToken> securities;
	@JoinColumn(name = "ProductID",
	            referencedColumnName = "ProductID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Product productID;
	@JoinColumn(name = "ProductTypeID",
	            referencedColumnName = "ProductTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private ProductType productTypeID;
	
	public ProductXProductType()
	{
	
	}
	
	public ProductXProductType(Long productXProductTypeID)
	{
		this.id = productXProductTypeID;
	}
	
	@Override
	protected ProductXProductTypeSecurityToken configureDefaultsForNewToken(ProductXProductTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	@Override
	public Long getId()
	{
		return this.id;
	}
	
	public List<ProductXProductTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public Product getProductID()
	{
		return this.productID;
	}
	
	public ProductType getProductTypeID()
	{
		return this.productTypeID;
	}
	
	@Override
	public ProductXProductType setId(Long id)
	{
		this.id = id;
		return this;
	}
	
	public ProductXProductType setSecurities(List<ProductXProductTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public ProductXProductType setProductID(Product productID)
	{
		this.productID = productID;
		return this;
	}
	
	public ProductXProductType setProductTypeID(ProductType productTypeID)
	{
		this.productTypeID = productTypeID;
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
		ProductXProductType that = (ProductXProductType) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public IProduct<?> getPrimary()
	{
		return getProductID();
	}
	
	@Override
	public IProductType<?> getSecondary()
	{
		return getProductTypeID();
	}
}
