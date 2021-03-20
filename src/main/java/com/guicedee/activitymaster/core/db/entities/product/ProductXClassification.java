package com.guicedee.activitymaster.core.db.entities.product;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.product.builders.ProductXClassificationQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Product", name = "ProductXClassification")
@XmlRootElement

@Access(FIELD)
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
		java.util.UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ProductXClassificationID")
	@org.hibernate.annotations.Type(type = "uuid-char")
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
