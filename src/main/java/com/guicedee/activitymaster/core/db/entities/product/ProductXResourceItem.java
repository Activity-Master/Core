package com.guicedee.activitymaster.core.db.entities.product;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.product.builders.ProductXResourceItemQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IProduct;
import com.guicedee.activitymaster.core.services.dto.IResourceItem;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Product",
		name = "ProductXResourceItem")
@XmlRootElement
@Access(FIELD)
public class ProductXResourceItem
		extends WarehouseClassificationRelationshipTable<Product,
				                                                ResourceItem,
				                                                ProductXResourceItem,
				                                                ProductXResourceItemQueryBuilder,
				                                                java.util.UUID,
				                                                ProductXResourceItemSecurityToken,
				                                                IProduct<?>, IResourceItem<?>>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "ProductXResourceItemID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
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

	public ProductXResourceItem(UUID productXResourceItemID)
	{
		id = productXResourceItemID;
	}

	@Override
	protected ProductXResourceItemSecurityToken configureDefaultsForNewToken(ProductXResourceItemSecurityToken stAdmin, ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public java.util.UUID getId()
	{
		return id;
	}

	public List<ProductXResourceItemSecurityToken> getSecurities()
	{
		return securities;
	}

	public Product getProductID()
	{
		return productID;
	}

	public ResourceItem getResourceItemID()
	{
		return resourceItemID;
	}

	@Override
	public ProductXResourceItem setId(java.util.UUID id)
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
