package com.guicedee.activitymaster.fsdm.db.entities.product;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.product.builders.ProductXProductQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
@Table(schema = "Product",
       name = "ProductXProduct")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ProductXProduct
		extends WarehouseClassificationRelationshipTable<Product,
		Product,
		ProductXProduct,
		ProductXProductQueryBuilder,
		UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ProductXProductID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	
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
	public UUID getId()
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
	public ProductXProduct setId(UUID id)
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
	public Product getPrimary()
	{
		return getParentProductID();
	}
	
	@Override
	public Product getSecondary()
	{
		return getChildProductID();
	}
}
