package com.guicedee.activitymaster.core.db.entities.rules;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.rules.builders.RulesXProductQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IProduct;
import com.guicedee.activitymaster.core.services.dto.IRules;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.FIELD;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Rules", name = "RulesXProduct")
@XmlRootElement

@Access(FIELD)@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
		java.util.UUID,
		RulesXProductSecurityToken,
		IRules<?>, IProduct<?>>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "RulesXProductID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
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
	
	@Override
	protected RulesXProductSecurityToken configureDefaultsForNewToken(RulesXProductSecurityToken stAdmin, ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	public List<RulesXProductSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public Rules getRulesID()
	{
		return this.rulesID;
	}
	
	public Product getProductID()
	{
		return this.productID;
	}
	
	public RulesXProduct setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public RulesXProduct setSecurities(List<RulesXProductSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public RulesXProduct setRulesID(Rules rulesID)
	{
		this.rulesID = rulesID;
		return this;
	}
	
	public RulesXProduct setProductID(Product productID)
	{
		this.productID = productID;
		return this;
	}
	
	@Override
	public IRules<?> getPrimary()
	{
		return getRulesID();
	}
	
	@Override
	public IProduct<?> getSecondary()
	{
		return getProductID();
	}
}
