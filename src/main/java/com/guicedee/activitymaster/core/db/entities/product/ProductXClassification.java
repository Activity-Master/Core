package com.guicedee.activitymaster.core.db.entities.product;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.product.builders.ProductXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IProduct;
import com.guicedee.activitymaster.core.services.dto.ISystems;

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
@Table(schema="Product",name = "ProductXClassification")
@XmlRootElement

@Access(FIELD)
public class ProductXClassification
		extends WarehouseClassificationRelationshipTable<Product,
				                                                Classification,
				                                                ProductXClassification,
				                                                ProductXClassificationQueryBuilder,
				                                                java.util.UUID,
				                                                ProductXClassificationSecurityToken,
				                                                IProduct<?>, IClassification<?>>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "ProductXClassificationID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

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

	public ProductXClassification(UUID productXClassificationID)
	{
		this.id = productXClassificationID;
	}

	@Override
	protected ProductXClassificationSecurityToken configureDefaultsForNewToken(ProductXClassificationSecurityToken stAdmin,  ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public java.util.UUID getId()
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

	public ProductXClassification setId(java.util.UUID id)
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
