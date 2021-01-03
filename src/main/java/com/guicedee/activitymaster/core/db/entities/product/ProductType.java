package com.guicedee.activitymaster.core.db.entities.product;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.product.builders.ProductTypeQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemXClassification;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.dto.*;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
@SuppressWarnings("unused")
@Entity
@Table(schema = "Product",
       name = "ProductType")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ProductType
		extends WarehouseSCDNameDescriptionTable<ProductType, ProductTypeQueryBuilder, java.util.UUID, ProductTypeSecurityToken>
		implements IProductType<ProductType>,
		           IActivityMasterEntity<ProductType>,
		           IContainsClassifications<ProductType, Classification, ProductTypeXClassification, IClassificationValue<?>, IProductType<?>,IClassification<?>,ProductType>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ProductTypeID")
	@JsonValue@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
	      max = 200)
	@Column(nullable = false,
	        length = 200,
	        name = "ProductTypeName")
		private String name;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
	      max = 200)
	@Column(nullable = false,
	        length = 200,
	        name = "ProductTypeDesc")
		private String description;
	
	@OneToMany(
			mappedBy = "productTypeID",
			fetch = FetchType.LAZY)
		private List<ProductXProductType> productXProductTypeList;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
		private List<ProductTypeSecurityToken> securities;
	
	
	@OneToMany(
			mappedBy = "productTypeID",
			fetch = FetchType.LAZY)
		private List<ProductTypeXClassification> classifications;
	
	
	public ProductType()
	{
	
	}
	
	public ProductType(UUID productTypeID)
	{
		this.id = productTypeID;
	}
	
	public ProductType(UUID productTypeID, String productTypName, String productTypeDesc)
	{
		this.id = productTypeID;
		this.name = productTypName;
		this.description = productTypeDesc;
	}
	
	@Override
	protected ProductTypeSecurityToken configureDefaultsForNewToken(ProductTypeSecurityToken stAdmin,  ISystems<?> enterprise, ISystems<?> activityMasterSystem)
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
	public java.util.UUID getId()
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
	public ProductType setId(java.util.UUID id)
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
	
	
	@Override
	public void configureForClassification(ProductTypeXClassification classificationLink, ISystems<?> system)
	{
		classificationLink.setProductTypeID(this);
	}
}
