package com.armineasy.activitymaster.activitymaster.db.entities.product;

import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXProduct;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXProduct;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXProduct;
import com.armineasy.activitymaster.activitymaster.db.entities.product.builders.ProductQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsResourceItems;
import com.armineasy.activitymaster.activitymaster.services.classifications.product.IProductClassification;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.*;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IResourceType;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;
import static javax.persistence.FetchType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "Product")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class Product
		extends WarehouseSCDNameDescriptionTable<Product, ProductQueryBuilder, Long, ProductSecurityToken>
		implements IContainsClassifications<Product, Classification, ProductXClassification, IProductClassification<?>,Product>,
				           IContainsResourceItems<Product, ResourceItem, ProductXResourceItem, IResourceItemClassification<?>,IProduct<?>, IResourceItem<?>, Product>,
				           IActivityMasterEntity<Product>,
				           IProduct<Product>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ProductID")
	private Long id;
	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 150)
	@Column(nullable = false,
			length = 150,
			name = "ProductName")
	private String name;
	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 250)
	@Column(nullable = false,
			length = 250,
			name = "ProductDesc")
	private String description;
	@Basic(optional = false,fetch = EAGER)
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

	public Product(Long productID)
	{
		this.id = productID;
	}

	public Product(Long productID, String productName, String productDesc, String productCode)
	{
		this.id = productID;
		this.name = productName;
		this.description = productDesc;
		this.productCode = productCode;
	}

	@Override
	protected ProductSecurityToken configureDefaultsForNewToken(ProductSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
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

	public List<ProductXClassification> getClassifications()
	{
		return this.classifications;
	}

	public List<ProductSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public List<ArrangementXProduct> getArrangements()
	{
		return this.arrangements;
	}

	public List<ProductXResourceItem> getResources()
	{
		return this.resources;
	}

	public List<InvolvedPartyXProduct> getParties()
	{
		return this.parties;
	}

	public List<EventXProduct> getEvents()
	{
		return this.events;
	}

	public List<ProductXProduct> getProductXProductList()
	{
		return this.productXProductList;
	}

	public List<ProductXProduct> getProductXProductList1()
	{
		return this.productXProductList1;
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

	public Long getId()
	{
		return this.id;
	}

	public @NotNull @Size(min = 1,
			max = 150) String getName()
	{
		return this.name;
	}

	public @NotNull @Size(min = 1,
			max = 250) String getDescription()
	{
		return this.description;
	}

	public @NotNull @Size(min = 1,
			max = 10) String getProductCode()
	{
		return this.productCode;
	}

	public Product setId(Long id)
	{
		this.id = id;
		return this;
	}

	public Product setName(@NotNull @Size(min = 1,
			max = 150) String name)
	{
		this.name = name;
		return this;
	}

	public Product setDescription(@NotNull @Size(min = 1,
			max = 250) String description)
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


}
