/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.involvedparty;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyXProductQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IInvolvedParty;
import com.guicedee.activitymaster.core.services.dto.IProduct;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Party",name = "InvolvedPartyXProduct")
@XmlRootElement

@Access(FIELD)
public class InvolvedPartyXProduct
		extends WarehouseClassificationRelationshipTable<InvolvedParty,
				                                                Product,
						                                                InvolvedPartyXProduct,
				                                                InvolvedPartyXProductQueryBuilder,
						                                                Long,
						                                                InvolvedPartyXProductSecurityToken,
				                                                IInvolvedParty<?>, IProduct<?>
						                                                >
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "InvolvedPartyXProductID")
	private Long id;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXProductSecurityToken> securities;
	
	@JoinColumn(name = "InvolvedPartyID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedParty involvedPartyID;

	@OneToOne(mappedBy = "involvedPartyXProduct1",
			fetch = FetchType.LAZY)
	private InvolvedPartyXProduct involvedPartyXProduct;
	@JoinColumn(name = "InvolvedPartyXProductID",
			referencedColumnName = "InvolvedPartyXProductID",
			nullable = false,
			insertable = false,
			updatable = false)
	@OneToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedPartyXProduct involvedPartyXProduct1;
	@JoinColumn(name = "ProductID",
			referencedColumnName = "ProductID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Product productID;

	public InvolvedPartyXProduct()
	{

	}

	public InvolvedPartyXProduct(Long involvedPartyXProductID)
	{
		this.id = involvedPartyXProductID;
	}

	@Override
	protected InvolvedPartyXProductSecurityToken configureDefaultsForNewToken(InvolvedPartyXProductSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
	{
		return this.id;
	}

	public List<InvolvedPartyXProductSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public InvolvedParty getInvolvedPartyID()
	{
		return this.involvedPartyID;
	}

	public InvolvedPartyXProduct getInvolvedPartyXProduct()
	{
		return this.involvedPartyXProduct;
	}

	public InvolvedPartyXProduct getInvolvedPartyXProduct1()
	{
		return this.involvedPartyXProduct1;
	}

	public Product getProductID()
	{
		return this.productID;
	}

	public InvolvedPartyXProduct setId(Long id)
	{
		this.id = id;
		return this;
	}

	public InvolvedPartyXProduct setSecurities(List<InvolvedPartyXProductSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public InvolvedPartyXProduct setInvolvedPartyID(InvolvedParty involvedPartyID)
	{
		this.involvedPartyID = involvedPartyID;
		return this;
	}

	public InvolvedPartyXProduct setInvolvedPartyXProduct(InvolvedPartyXProduct involvedPartyXProduct)
	{
		this.involvedPartyXProduct = involvedPartyXProduct;
		return this;
	}

	public InvolvedPartyXProduct setInvolvedPartyXProduct1(InvolvedPartyXProduct involvedPartyXProduct1)
	{
		this.involvedPartyXProduct1 = involvedPartyXProduct1;
		return this;
	}

	public InvolvedPartyXProduct setProductID(Product productID)
	{
		this.productID = productID;
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
		InvolvedPartyXProduct that = (InvolvedPartyXProduct) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IInvolvedParty<?> getPrimary()
	{
		return getInvolvedPartyID();
	}

	@Override
	public IProduct<?> getSecondary()
	{
		return getProductID();
	}
}
