package com.guicedee.activitymaster.core.db.entities.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXProduct;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.events.EventXProduct;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXProduct;
import com.guicedee.activitymaster.core.db.entities.product.builders.ProductQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.product.builders.ProductXProductTypeQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.capabilities.IContainsProductTypes;
import com.guicedee.activitymaster.core.services.capabilities.IContainsResourceItems;
import com.guicedee.activitymaster.core.services.classifications.product.IProductClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.enumtypes.IProductTypeValue;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.guicedinjection.GuiceContext;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;
import static javax.persistence.FetchType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(schema = "Product",
       name = "Product")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
public class Product
		extends WarehouseSCDNameDescriptionTable<Product, ProductQueryBuilder, Long, ProductSecurityToken>
		implements IContainsClassifications<Product, Classification, ProductXClassification, IProductClassification<?>, IProduct<?>, IClassification<?>, Product>,
		           IContainsResourceItems<Product, ResourceItem, ProductXResourceItem, IClassificationValue<?>, IProduct<?>, IResourceItem<?>, Product>,
		           IContainsProductTypes<Product,ProductType, ProductXProductType, IClassificationValue<?>, IProductTypeValue<?>,IProduct<?>,IProductType<?>,Product>,
		           IActivityMasterEntity<Product>,
		           IProduct<Product>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
	        name = "ProductID")
	@JsonValue
	private Long id;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 150)
	@Column(nullable = false,
	        length = 150,
	        name = "ProductName")
	@JsonIgnore
	private String name;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 250)
	@Column(nullable = false,
	        length = 250,
	        name = "ProductDesc")
	@JsonIgnore
	private String description;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 10)
	@Column(nullable = false,
	        length = 10,
	        name = "ProductCode")
	@JsonIgnore
	private String productCode;
	
	@OneToMany(
			mappedBy = "productID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ProductXClassification> classifications;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ProductSecurityToken> securities;
	
	@OneToMany(
			mappedBy = "productID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ArrangementXProduct> arrangements;
	@OneToMany(
			mappedBy = "productID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ProductXResourceItem> resources;
	@OneToMany(
			mappedBy = "productID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<InvolvedPartyXProduct> parties;
	@OneToMany(
			mappedBy = "productID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventXProduct> events;
	@OneToMany(
			mappedBy = "childProductID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ProductXProduct> productXProductList;
	@OneToMany(
			mappedBy = "parentProductID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ProductXProduct> productXProductList1;
	
	public Product()
	{
	
	}
	
	public Product(Long productID)
	{
		id = productID;
	}
	
	public Product(Long productID, String productName, String productDesc, String productCode)
	{
		id = productID;
		name = productName;
		description = productDesc;
		this.productCode = productCode;
	}
	
	@Override
	protected ProductSecurityToken configureDefaultsForNewToken(ProductSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	@Override
	public void configureForClassification(ProductXClassification classificationLink, IEnterprise<?> enterprise)
	{
		classificationLink.setProductID(this);
	}
	
	@Override
	public void configureResourceItemLinkValue(ProductXResourceItem linkTable, Product primary, ResourceItem secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setResourceItemID(secondary);
		linkTable.setProductID(this);
	}
	
	@Override
	public void configureResourceItemAddable(ProductXResourceItem linkTable, Product primary, ResourceItem secondary, IClassificationValue<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setResourceItemID(secondary);
		linkTable.setProductID(this);
	}
	
	public List<ProductXClassification> getClassifications()
	{
		return classifications;
	}
	
	public List<ProductSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public List<ArrangementXProduct> getArrangements()
	{
		return arrangements;
	}
	
	public List<ProductXResourceItem> getResources()
	{
		return resources;
	}
	
	public List<InvolvedPartyXProduct> getParties()
	{
		return parties;
	}
	
	public List<EventXProduct> getEvents()
	{
		return events;
	}
	
	public List<ProductXProduct> getProductXProductList()
	{
		return productXProductList;
	}
	
	public List<ProductXProduct> getProductXProductList1()
	{
		return productXProductList1;
	}
	
	public Product setClassifications(List<ProductXClassification> classifications)
	{
		this.classifications = classifications;
		return this;
	}
	
	public Product setSecurities(List<ProductSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public Product setArrangements(List<ArrangementXProduct> arrangements)
	{
		this.arrangements = arrangements;
		return this;
	}
	
	public Product setResources(List<ProductXResourceItem> resources)
	{
		this.resources = resources;
		return this;
	}
	
	public Product setParties(List<InvolvedPartyXProduct> parties)
	{
		this.parties = parties;
		return this;
	}
	
	public Product setEvents(List<EventXProduct> events)
	{
		this.events = events;
		return this;
	}
	
	public Product setProductXProductList(List<ProductXProduct> productXProductList)
	{
		this.productXProductList = productXProductList;
		return this;
	}
	
	public Product setProductXProductList1(List<ProductXProduct> productXProductList1)
	{
		this.productXProductList1 = productXProductList1;
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
		Product product = (Product) o;
		return Objects.equals(getName(), product.getName()) &&
				Objects.equals(getProductCode(), product.getProductCode());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getName(), getProductCode());
	}
	
	@Override
	public String toString()
	{
		return "Product - " + getProductCode() + " - " + getName();
	}
	
	@Override
	public Long getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public @NotNull @Size(min = 1,
	                      max = 10) String getProductCode()
	{
		return productCode;
	}
	
	@Override
	public Product setId(Long id)
	{
		this.id = id;
		return this;
	}
	
	public Product setName(String name)
	{
		this.name = name;
		return this;
	}
	
	public Product setDescription(String description)
	{
		this.description = description;
		return this;
	}
	
	public Product setProductCode(@NotNull @Size(min = 1,
	                                             max = 10) String productCode)
	{
		this.productCode = productCode;
		return this;
	}
	
	@Override
	public void configureProductTypeLinkValue(ProductXProductType linkTable, Product primary, ProductType secondary, IClassificationValue<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setProductID(primary);
		linkTable.setProductTypeID(secondary);
		IClassificationService<?> service = GuiceContext.get(IClassificationService.class);
		linkTable.setClassificationID((Classification) service.find(classificationValue.classificationName(), enterprise));
		linkTable.setValue(Strings.nullToEmpty(value));
	}
}
