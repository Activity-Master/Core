package com.armineasy.activitymaster.activitymaster.db.entities.product;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.product.builders.ProductXResourceItemQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ProductXResourceItem")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ProductXResourceItem
		extends WarehouseClassificationRelationshipTable<Product, ResourceItem, ProductXResourceItem, ProductXResourceItemQueryBuilder, Long, ProductXResourceItemSecurityToken>
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
	protected ProductXResourceItemSecurityToken configureDefaultsForNewToken(ProductXResourceItemSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "ProductXResourceItem(id=" + this.getId() + ", securities=" + this.getSecurities() + ", productID=" + this.getProductID() + ", resourceItemID=" +
		       this.getResourceItemID() + ")";
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof ProductXResourceItem))
		{
			return false;
		}
		final ProductXResourceItem other = (ProductXResourceItem) o;
		if (!other.canEqual((Object) this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
		{
			return false;
		}
		return true;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof ProductXResourceItem;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}
