package com.armineasy.activitymaster.activitymaster.db.entities.product;

import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXProduct;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConcept;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXProduct;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXProduct;
import com.armineasy.activitymaster.activitymaster.db.entities.product.builders.ProductQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsResourceItems;
import com.armineasy.activitymaster.activitymaster.services.classifications.product.IProductClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

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
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class Product
		extends WarehouseSCDNameDescriptionTable<Product, ProductQueryBuilder, Long, ProductSecurityToken>
		implements IContainsClassifications<Product, Classification, ProductXClassification, IProductClassification>,
				           IContainsResourceItems<Product, ResourceItem, ProductXResourceItem>,
				           IActivityMasterEntity<Product>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ProductID")
	@Getter
	@Setter
	private Long id;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 150)
	@Column(nullable = false,
			length = 150,
			name = "ProductName")
	@Getter
	@Setter
	private String name;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 250)
	@Column(nullable = false,
			length = 250,
			name = "ProductDesc")
	@Getter
	@Setter
	private String description;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 10)
	@Column(nullable = false,
			length = 10,
			name = "ProductCode")
	@Getter
	@Setter
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
	protected ProductSecurityToken configureDefaultsForNewToken(ProductSecurityToken stAdmin, IEnterprise enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public void configureForClassification(ProductXClassification classificationLink, Enterprise enterprise)
	{
		classificationLink.setProductID(this);
	}

	@Override
	public void setMyResourceItemLinkValue(ProductXResourceItem classificationLink, ResourceItem resourceItem, Enterprise enterprise)
	{
		classificationLink.setResourceItemID(resourceItem);
		classificationLink.setProductID(this);
	}
}
