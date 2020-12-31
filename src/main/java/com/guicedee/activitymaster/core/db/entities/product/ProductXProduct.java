package com.guicedee.activitymaster.core.db.entities.product;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.product.builders.ProductXProductQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IProduct;
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
		name = "ProductXProduct")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
public class ProductXProduct
		extends WarehouseClassificationRelationshipTable<Product,
				                                                Product,
				                                                ProductXProduct,
				                                                ProductXProductQueryBuilder,
				                                                java.util.UUID,
				                                                ProductXProductSecurityToken,
				                                                IProduct<?>, IProduct<?>>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
			name = "ProductXProductID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ProductXProductSecurityToken> securities;

	@JoinColumn(name = "ChildProductID",
			referencedColumnName = "ProductID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Product childProductID;
	@JoinColumn(name = "ParentProductID",
			referencedColumnName = "ProductID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Product parentProductID;

	public ProductXProduct()
	{

	}

	public ProductXProduct(UUID productXProductID)
	{
		id = productXProductID;
	}

	@Override
	protected ProductXProductSecurityToken configureDefaultsForNewToken(ProductXProductSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public java.util.UUID getId()
	{
		return id;
	}

	public List<ProductXProductSecurityToken> getSecurities()
	{
		return securities;
	}

	public Product getChildProductID()
	{
		return childProductID;
	}

	public Product getParentProductID()
	{
		return parentProductID;
	}

	@Override
	public ProductXProduct setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}

	public ProductXProduct setSecurities(List<ProductXProductSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public ProductXProduct setChildProductID(Product childProductID)
	{
		this.childProductID = childProductID;
		return this;
	}

	public ProductXProduct setParentProductID(Product parentProductID)
	{
		this.parentProductID = parentProductID;
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
		ProductXProduct that = (ProductXProduct) o;
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
		return getParentProductID();
	}

	@Override
	public IProduct<?> getSecondary()
	{
		return getChildProductID();
	}
}
