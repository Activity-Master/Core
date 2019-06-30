package com.armineasy.activitymaster.activitymaster.db.entities.product;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.product.builders.ProductXClassificationQueryBuilder;
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
@Table(name = "ProductXClassification")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ProductXClassification
		extends WarehouseClassificationRelationshipTable<Product, Classification, ProductXClassification, ProductXClassificationQueryBuilder, Long, ProductXClassificationSecurityToken>
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

	public String toString()
	{
		return "ProductXClassification(id=" + this.getId() + ", productID=" + this.getProductID() + ", securities=" + this.getSecurities() + ")";
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof ProductXClassification))
		{
			return false;
		}
		final ProductXClassification other = (ProductXClassification) o;
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
		return other instanceof ProductXClassification;
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
