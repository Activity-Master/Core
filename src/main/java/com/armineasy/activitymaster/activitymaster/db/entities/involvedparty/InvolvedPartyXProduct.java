/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.involvedparty;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartyXProductQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.product.Product;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "InvolvedPartyXProduct")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class InvolvedPartyXProduct
		extends WarehouseClassificationRelationshipTable<InvolvedParty, Product, InvolvedPartyXProduct, InvolvedPartyXProductQueryBuilder, Long, InvolvedPartyXProductSecurityToken>
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

	@JoinColumn(name = "ValueTypeClassificationID",
			referencedColumnName = "ClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Classification valueTypeClassificationID;

	@JoinColumn(name = "InvolvedPartyID",
			referencedColumnName = "InvolvedPartyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private InvolvedParty involvedPartyID;

	@OneToOne(cascade =
			          {
					          CascadeType.DETACH
			          },
			mappedBy = "involvedPartyXProduct1",
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
	protected InvolvedPartyXProductSecurityToken configureDefaultsForNewToken(InvolvedPartyXProductSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "InvolvedPartyXProduct(id=" + this.getId() + ", securities=" + this.getSecurities() + ", valueTypeClassificationID=" + this.getValueTypeClassificationID() +
		       ", involvedPartyID=" + this.getInvolvedPartyID() + ", involvedPartyXProduct=" + this.getInvolvedPartyXProduct() + ", involvedPartyXProduct1=" +
		       this.getInvolvedPartyXProduct1() + ", productID=" + this.getProductID() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public List<InvolvedPartyXProductSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public Classification getValueTypeClassificationID()
	{
		return this.valueTypeClassificationID;
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

	public InvolvedPartyXProduct setValueTypeClassificationID(Classification valueTypeClassificationID)
	{
		this.valueTypeClassificationID = valueTypeClassificationID;
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof InvolvedPartyXProduct))
		{
			return false;
		}
		final InvolvedPartyXProduct other = (InvolvedPartyXProduct) o;
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
		return other instanceof InvolvedPartyXProduct;
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
