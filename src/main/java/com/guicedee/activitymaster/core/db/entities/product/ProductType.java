package com.guicedee.activitymaster.core.db.entities.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.product.builders.ProductTypeQueryBuilder;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IProductType;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(schema = "Product",
       name = "ProductType")
@XmlRootElement

@Access(FIELD)
public class ProductType
		extends WarehouseSCDNameDescriptionTable<ProductType, ProductTypeQueryBuilder, Long, ProductTypeSecurityToken>
		implements IProductType<ProductType>,
		           IActivityMasterEntity<ProductType>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
	        name = "ProductTypeID")
	@JsonValue
	private Long id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
	      max = 200)
	@Column(nullable = false,
	        length = 200,
	        name = "ProductTypeName")
	@JsonIgnore
	private String name;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
	      max = 200)
	@Column(nullable = false,
	        length = 200,
	        name = "ProductTypeDesc")
	@JsonIgnore
	private String description;
	
	@OneToMany(
			mappedBy = "productTypeID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ProductXProductType> productXProductTypeList;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ProductTypeSecurityToken> securities;
	
	public ProductType()
	{
	
	}
	
	public ProductType(Long productTypeID)
	{
		this.id = productTypeID;
	}
	
	public ProductType(Long productTypeID, String productTypName, String productTypeDesc)
	{
		this.id = productTypeID;
		this.name = productTypName;
		this.description = productTypeDesc;
	}
	
	@Override
	protected ProductTypeSecurityToken configureDefaultsForNewToken(ProductTypeSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	public List<ProductXProductType> getProductXProductTypeList()
	{
		return this.productXProductTypeList;
	}
	
	public List<ProductTypeSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public ProductType setProductXProductTypeList(List<ProductXProductType> productXProductTypeList)
	{
		this.productXProductTypeList = productXProductTypeList;
		return this;
	}
	
	public ProductType setSecurities(List<ProductTypeSecurityToken> securities)
	{
		this.securities = securities;
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
		ProductType productType = (ProductType) o;
		return Objects.equals(getName(), productType.getName());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public String toString()
	{
		return "ProductType - " + getName();
	}
	
	@Override
	public Long getId()
	{
		return this.id;
	}
	
	@Override
	public @NotNull @Size(min = 1,
	                      max = 200) String getName()
	{
		return this.name;
	}
	
	@Override
	public @NotNull @Size(min = 1,
	                      max = 200) String getDescription()
	{
		return this.description;
	}
	
	@Override
	public ProductType setId(Long id)
	{
		this.id = id;
		return this;
	}
	
	@Override
	public ProductType setName(@NotNull @Size(min = 1,
	                                          max = 200) String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	public ProductType setDescription(@NotNull @Size(min = 1,
	                                                 max = 200) String description)
	{
		this.description = description;
		return this;
	}
	
	@Override
	public String name()
	{
		return getName();
	}
	
	@Override
	public String classificationValue()
	{
		return getDescription();
	}
}
