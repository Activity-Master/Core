package com.guicedee.activitymaster.core.db.entities.product;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.product.builders.ProductTypeXClassificationQueryBuilder;
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
@Table(schema = "Product", name = "ProductTypeXClassification")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ProductTypeXClassification
		extends WarehouseClassificationRelationshipTable<ProductType,
		Classification,
		ProductTypeXClassification,
		ProductTypeXClassificationQueryBuilder,
		UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ProductTypeXClassificationID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
	@JoinColumn(name = "ProductTypeID",
	            referencedColumnName = "ProductTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private ProductType productTypeID;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ProductTypeXClassificationSecurityToken> securities;
	
	public ProductTypeXClassification()
	{
	
	}
	
	public ProductTypeXClassification(UUID productXClassificationID)
	{
		this.id = productXClassificationID;
	}
	
	
	public UUID getId()
	{
		return this.id;
	}
	
	public ProductType getProductTypeID()
	{
		return this.productTypeID;
	}
	
	public List<ProductTypeXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public ProductTypeXClassification setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public ProductTypeXClassification setProductTypeID(ProductType productTypeID)
	{
		this.productTypeID = productTypeID;
		return this;
	}
	
	public ProductTypeXClassification setSecurities(List<ProductTypeXClassificationSecurityToken> securities)
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
		ProductTypeXClassification that = (ProductTypeXClassification) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public ProductType getPrimary()
	{
		return getProductTypeID();
	}
	
	@Override
	public Classification getSecondary()
	{
		return getSecondary();
	}
}
