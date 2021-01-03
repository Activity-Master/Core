/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.arrangement;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementXProductQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.services.dto.IArrangement;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IProduct;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Arrangement",name = "ArrangementXProduct")
@XmlRootElement
@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ArrangementXProduct
		extends WarehouseClassificationRelationshipTable<Arrangement,
				                                                Product,
						                                                ArrangementXProduct,
				                                                ArrangementXProductQueryBuilder,
						                                                java.util.UUID,
						                                                ArrangementXProductSecurityToken,
				                                                IArrangement<?>, IProduct<?>>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
			name = "ArrangementXProductID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
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

	public ArrangementXProduct(UUID arrangementXProductID)
	{
		this.id = arrangementXProductID;
	}

	@Override
	protected ArrangementXProductSecurityToken configureDefaultsForNewToken(ArrangementXProductSecurityToken stAdmin, ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public java.util.UUID getId()
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

	public ArrangementXProduct setId(java.util.UUID id)
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
		ArrangementXProduct that = (ArrangementXProduct) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IArrangement<?> getPrimary()
	{
		return getArrangementID();
	}

	@Override
	public IProduct<?> getSecondary()
	{
		return getProductID();
	}
}
