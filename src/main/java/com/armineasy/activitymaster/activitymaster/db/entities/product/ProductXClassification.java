package com.armineasy.activitymaster.activitymaster.db.entities.product;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.product.builders.ProductXClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IProduct;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
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
	protected ProductXClassificationSecurityToken configureDefaultsForNewToken(ProductXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
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
