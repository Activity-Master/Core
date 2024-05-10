package com.guicedee.activitymaster.fsdm.db.entities.product;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProductType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXProduct;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXProduct;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXProduct;
import com.guicedee.activitymaster.fsdm.db.entities.product.builders.ProductQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.FetchType.*;

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
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class Product
		extends WarehouseSCDNameDescriptionTable<Product, ProductQueryBuilder, java.lang.String, ProductSecurityToken>
		implements IProduct<Product, ProductQueryBuilder>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ProductID")
	@JsonValue
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 150)
	@Column(nullable = false,
	        length = 150,
	        name = "ProductName")
	private String name;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 250)
	@Column(nullable = false,
	        length = 250,
	        name = "ProductDesc")
	private String description;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 10)
	@Column(nullable = false,
	        length = 10,
	        name = "ProductCode")
	private String productCode;
	
	@OneToMany(
			mappedBy = "productID",
			fetch = FetchType.LAZY)
	private List<ProductXClassification> classifications;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ProductSecurityToken> securities;
	
	@OneToMany(
			mappedBy = "productID",
			fetch = FetchType.LAZY)
	private List<ArrangementXProduct> arrangements;
	@OneToMany(
			mappedBy = "productID",
			fetch = FetchType.LAZY)
	private List<ProductXResourceItem> resources;
	@OneToMany(
			mappedBy = "productID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXProduct> parties;
	@OneToMany(
			mappedBy = "productID",
			fetch = FetchType.LAZY)
	private List<EventXProduct> events;
	@OneToMany(
			mappedBy = "childProductID",
			fetch = FetchType.LAZY)
	private List<ProductXProduct> productXProductList;
	@OneToMany(
			mappedBy = "parentProductID",
			fetch = FetchType.LAZY)
	private List<ProductXProduct> productXProductList1;
	
	public Product()
	{
	
	}
	
	public Product(java.lang.String id)
	{
		this.id = id;
	}
	
	public Product(java.lang.String id, String productName, String productDesc, String productCode)
	{
		this.id = id;
		name = productName;
		description = productDesc;
		this.productCode = productCode;
	}
	
	public List<ProductXClassification> getClassifications()
	{
		return classifications;
	}
	
	public Product setClassifications(List<ProductXClassification> classifications)
	{
		this.classifications = classifications;
		return this;
	}
	
	public List<ProductSecurityToken> getSecurities()
	{
		return securities;
	}
	
	public Product setSecurities(List<ProductSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public List<ArrangementXProduct> getArrangements()
	{
		return arrangements;
	}
	
	public Product setArrangements(List<ArrangementXProduct> arrangements)
	{
		this.arrangements = arrangements;
		return this;
	}
	
	public List<ProductXResourceItem> getResources()
	{
		return resources;
	}
	
	public Product setResources(List<ProductXResourceItem> resources)
	{
		this.resources = resources;
		return this;
	}
	
	public List<InvolvedPartyXProduct> getParties()
	{
		return parties;
	}
	
	public Product setParties(List<InvolvedPartyXProduct> parties)
	{
		this.parties = parties;
		return this;
	}
	
	public List<EventXProduct> getEvents()
	{
		return events;
	}
	
	public Product setEvents(List<EventXProduct> events)
	{
		this.events = events;
		return this;
	}
	
	public List<ProductXProduct> getProductXProductList()
	{
		return productXProductList;
	}
	
	public Product setProductXProductList(List<ProductXProduct> productXProductList)
	{
		this.productXProductList = productXProductList;
		return this;
	}
	
	public List<ProductXProduct> getProductXProductList1()
	{
		return productXProductList1;
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
		return getProductCode();
	}
	
	@Override
	public java.lang.String getId()
	{
		return id;
	}
	
	@Override
	public Product setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Product setName(String name)
	{
		this.name = name;
		return this;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public Product setDescription(String description)
	{
		this.description = description;
		return this;
	}
	
	public @NotNull @Size(min = 1,
	                      max = 10) String getProductCode()
	{
		return productCode;
	}
	
	public Product setProductCode(@NotNull @Size(min = 1,
	                                             max = 10) String productCode)
	{
		this.productCode = productCode;
		return this;
	}
	
	@Override
	public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable, IClassification<?, ?> classificationValue, ISystems<?, ?> system)
	{
		((ProductXClassification) linkTable)
		                            .setProductID(this);
	}
	
	@Override
	public void configureProductTypeLinkValue(IWarehouseRelationshipTable linkTable, Product primary, IProductType<?, ?> secondary, IClassification<?, ?> classificationValue, String value, IEnterprise<?, ?> enterprise)
	{
		ProductXProductType pxt = (ProductXProductType) linkTable;
		pxt.setProductID(primary);
		pxt.setProductTypeID((ProductType) secondary);
		linkTable.setClassificationID(classificationValue);
		linkTable.setValue(Strings.nullToEmpty(value));
	}
	
	@Override
	public void configureResourceItemAddable(IWarehouseRelationshipTable linkTable, Product primary, IResourceItem<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
	{
		ProductXResourceItem pxt = (ProductXResourceItem) linkTable;
		pxt.setProductID(primary);
		pxt.setResourceItemID((ResourceItem) secondary);
		linkTable.setClassificationID(classificationValue);
		linkTable.setValue(Strings.nullToEmpty(value));
	}
}
