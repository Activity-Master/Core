package com.guicedee.activitymaster.core.db.entities.product;


import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.core.db.entities.product.builders.ProductXProductTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IProduct;
import com.guicedee.activitymaster.core.services.dto.IProductType;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IProductTypeValue;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;

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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ProductXProductType
		extends WarehouseClassificationRelationshipTypesTable<Product,
				ProductType,
				ProductXProductType,
				ProductXProductTypeQueryBuilder,
				IProductTypeValue<?>,
				java.util.UUID,
				ProductXProductTypeSecurityToken,
				IProduct<?>, IProductType<?>>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
	        name = "ProductXProductTypeID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
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
	
	public ProductXProductType(UUID productXProductTypeID)
	{
		this.id = productXProductTypeID;
	}
	
	@Override
	protected ProductXProductTypeSecurityToken configureDefaultsForNewToken(ProductXProductTypeSecurityToken stAdmin,  ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	@Override
	public java.util.UUID getId()
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
	public ProductXProductType setId(java.util.UUID id)
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
