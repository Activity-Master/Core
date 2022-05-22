package com.guicedee.activitymaster.fsdm.db.entities.product;


import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.fsdm.db.entities.product.builders.ProductXProductTypeQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.*;

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
@Access(AccessType.FIELD)
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
		UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ProductXProductTypeID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
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
	public UUID getId()
	{
		return this.id;
	}
	
	@Override
	public ProductXProductType setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public List<ProductXProductTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public ProductXProductType setSecurities(List<ProductXProductTypeSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public Product getProductID()
	{
		return this.productID;
	}
	
	public ProductXProductType setProductID(Product productID)
	{
		this.productID = productID;
		return this;
	}
	
	public ProductType getProductTypeID()
	{
		return this.productTypeID;
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
	public Product getPrimary()
	{
		return getProductID();
	}
	
	@Override
	public ProductType getSecondary()
	{
		return getProductTypeID();
	}
}
