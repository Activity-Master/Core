package com.guicedee.activitymaster.fsdm.db.entities.rules;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;
import com.guicedee.activitymaster.fsdm.db.entities.rules.builders.RulesXProductQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Rules", name = "RulesXProduct")
@XmlRootElement

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class RulesXProduct
		extends WarehouseClassificationRelationshipTable<Rules,
		Product,
		RulesXProduct,
		RulesXProductQueryBuilder,
		UUID>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesXProductID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private UUID id;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<RulesXProductSecurityToken> securities;
	
	@JoinColumn(name = "RulesID",
	            referencedColumnName = "RulesID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Rules rulesID;
	@JoinColumn(name = "ProductID",
	            referencedColumnName = "ProductID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Product productID;
	
	public RulesXProduct()
	{
	
	}
	
	public RulesXProduct(UUID RulesXProductID)
	{
		this.id = RulesXProductID;
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public RulesXProduct setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public List<RulesXProductSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public RulesXProduct setSecurities(List<RulesXProductSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public Rules getRulesID()
	{
		return this.rulesID;
	}
	
	public RulesXProduct setRulesID(Rules rulesID)
	{
		this.rulesID = rulesID;
		return this;
	}
	
	public Product getProductID()
	{
		return this.productID;
	}
	
	public RulesXProduct setProductID(Product productID)
	{
		this.productID = productID;
		return this;
	}
	
	@Override
	public Rules getPrimary()
	{
		return getRulesID();
	}
	
	@Override
	public Product getSecondary()
	{
		return getProductID();
	}
}
