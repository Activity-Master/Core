package com.guicedee.activitymaster.core.db.entities.product;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.product.builders.ProductXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IProduct;
import com.guicedee.activitymaster.core.services.dto.ISystems;

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
@Table(name = "ProductXClassification")
@XmlRootElement

@Access(FIELD)
public class ProductXClassification
		extends WarehouseClassificationRelationshipTable<Product,
				                                                Classification,
				                                                ProductXClassification,
				                                                ProductXClassificationQueryBuilder,
				                                                Long,
				                                                ProductXClassificationSecurityToken,
				                                                IProduct<?>, IClassification<?>>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ProductXClassificationID")
	private Long id;

	@JoinColumn(name = "ProductID",
			referencedColumnName = "ProductID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Product productID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ProductXClassificationSecurityToken> securities;

	public ProductXClassification()
	{

	}

	public ProductXClassification(Long productXClassificationID)
	{
		this.id = productXClassificationID;
	}

	@Override
	protected ProductXClassificationSecurityToken configureDefaultsForNewToken(ProductXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
	{
		return this.id;
	}

	public Product getProductID()
	{
		return this.productID;
	}

	public List<ProductXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public ProductXClassification setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ProductXClassification setProductID(Product productID)
	{
		this.productID = productID;
		return this;
	}

	public ProductXClassification setSecurities(List<ProductXClassificationSecurityToken> securities)
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
		ProductXClassification that = (ProductXClassification) o;
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
	public IClassification<?> getSecondary()
	{
		return getSecondary();
	}
}
