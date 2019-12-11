package com.guicedee.activitymaster.core.db.entities.product;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.product.builders.ProductXResourceItemQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IProduct;
import com.guicedee.activitymaster.core.services.dto.IResourceItem;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Product",name = "ProductXResourceItem")
@XmlRootElement

@Access(FIELD)
public class ProductXResourceItem
		extends WarehouseClassificationRelationshipTable<Product,
				                                                ResourceItem,
				                                                ProductXResourceItem,
				                                                ProductXResourceItemQueryBuilder,
				                                                Long,
				                                                ProductXResourceItemSecurityToken,
				                                                IProduct<?>, IResourceItem<?>>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ProductXResourceItemID")
	private Long id;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ProductXResourceItemSecurityToken> securities;
	@JoinColumn(name = "ProductID",
			referencedColumnName = "ProductID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Product productID;
	@JoinColumn(name = "ResourceItemID",
			referencedColumnName = "ResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ResourceItem resourceItemID;

	public ProductXResourceItem()
	{

	}

	public ProductXResourceItem(Long productXResourceItemID)
	{
		this.id = productXResourceItemID;
	}

	@Override
	protected ProductXResourceItemSecurityToken configureDefaultsForNewToken(ProductXResourceItemSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
	{
		return this.id;
	}

	public List<ProductXResourceItemSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public Product getProductID()
	{
		return this.productID;
	}

	public ResourceItem getResourceItemID()
	{
		return this.resourceItemID;
	}

	public ProductXResourceItem setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ProductXResourceItem setSecurities(List<ProductXResourceItemSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public ProductXResourceItem setProductID(Product productID)
	{
		this.productID = productID;
		return this;
	}

	public ProductXResourceItem setResourceItemID(ResourceItem resourceItemID)
	{
		this.resourceItemID = resourceItemID;
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
		ProductXResourceItem that = (ProductXResourceItem) o;
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
	public IResourceItem<?> getSecondary()
	{
		return getResourceItemID();
	}
}
