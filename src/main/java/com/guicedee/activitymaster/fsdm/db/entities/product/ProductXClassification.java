package com.guicedee.activitymaster.fsdm.db.entities.product;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.product.builders.ProductXClassificationQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Product", name = "ProductXClassification")
@XmlRootElement

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ProductXClassification
		extends WarehouseClassificationRelationshipTable<Product,
		Classification,
		ProductXClassification,
		ProductXClassificationQueryBuilder,
		java.lang.String, ProductXClassificationSecurityToken>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ProductXClassificationID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
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
	
	public ProductXClassification(java.lang.String productXClassificationID)
	{
		this.id = productXClassificationID;
	}
	
	@Override
	public void configureSecurityEntity(ProductXClassificationSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public ProductXClassification setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public Product getProductID()
	{
		return this.productID;
	}
	
	public ProductXClassification setProductID(Product productID)
	{
		this.productID = productID;
		return this;
	}
	
	public List<ProductXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
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
	public Product getPrimary()
	{
		return getProductID();
	}
	
	@Override
	public Classification getSecondary()
	{
		return getSecondary();
	}
}
