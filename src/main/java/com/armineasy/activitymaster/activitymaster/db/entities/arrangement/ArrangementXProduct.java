/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.arrangement;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementXProductQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.product.Product;
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
@Table(name = "ArrangementXProduct")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ArrangementXProduct
		extends WarehouseClassificationRelationshipTable<Arrangement, Product, ArrangementXProduct, ArrangementXProductQueryBuilder, Long, ArrangementXProductSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ArrangementXProductID")
	private Long id;
	@JoinColumn(name = "ArrangementID",
			referencedColumnName = "ArrangementID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Arrangement arrangementID;
	@JoinColumn(name = "ProductID",
			referencedColumnName = "ProductID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Product productID;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementXProductSecurityToken> securities;

	public ArrangementXProduct()
	{

	}

	public ArrangementXProduct(Long arrangementXProductID)
	{
		this.id = arrangementXProductID;
	}

	@Override
	protected ArrangementXProductSecurityToken configureDefaultsForNewToken(ArrangementXProductSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "ArrangementXProduct(id=" + this.getId() + ", arrangementID=" + this.getArrangementID() + ", productID=" + this.getProductID() + ", securities=" +
		       this.getSecurities() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public Arrangement getArrangementID()
	{
		return this.arrangementID;
	}

	public Product getProductID()
	{
		return this.productID;
	}

	public List<ArrangementXProductSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public ArrangementXProduct setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ArrangementXProduct setArrangementID(Arrangement arrangementID)
	{
		this.arrangementID = arrangementID;
		return this;
	}

	public ArrangementXProduct setProductID(Product productID)
	{
		this.productID = productID;
		return this;
	}

	public ArrangementXProduct setSecurities(List<ArrangementXProductSecurityToken> securities)
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
		if (!(o instanceof ArrangementXProduct))
		{
			return false;
		}
		final ArrangementXProduct other = (ArrangementXProduct) o;
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
		return other instanceof ArrangementXProduct;
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
