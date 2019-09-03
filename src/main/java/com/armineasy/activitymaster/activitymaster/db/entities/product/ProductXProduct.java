package com.armineasy.activitymaster.activitymaster.db.entities.product;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.product.builders.ProductXProductQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IProduct;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ProductXProduct")
@XmlRootElement

@Access(FIELD)
public class ProductXProduct
		extends WarehouseClassificationRelationshipTable<Product,
				                                                Product,
				                                                ProductXProduct,
				                                                ProductXProductQueryBuilder,
				                                                Long,
				                                                ProductXProductSecurityToken,
				                                                IProduct<?>,IProduct<?>>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ProductXProductID")
	private Long id;

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

	public ProductXProduct(Long productXProductID)
	{
		this.id = productXProductID;
	}

	@Override
	protected ProductXProductSecurityToken configureDefaultsForNewToken(ProductXProductSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
	{
		return this.id;
	}

	public List<ProductXProductSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public Product getChildProductID()
	{
		return this.childProductID;
	}

	public Product getParentProductID()
	{
		return this.parentProductID;
	}

	public ProductXProduct setId(Long id)
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
