/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXProductQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;
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
@Table(schema = "Arrangement", name = "ArrangementXProduct")
@XmlRootElement
@Access(AccessType.FIELD)
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
		java.lang.String,
		ArrangementXProductSecurityToken
		>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
	        name = "ArrangementXProductID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
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
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<ArrangementXProductSecurityToken> securities;
	
	public ArrangementXProduct()
	{
	
	}
	
	public ArrangementXProduct(java.lang.String arrangementXProductID)
	{
		this.id = arrangementXProductID;
	}
	
	@Override
	public void configureSecurityEntity(ArrangementXProductSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	public ArrangementXProduct setId(java.lang.String id)
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
	public Arrangement getPrimary()
	{
		return getArrangementID();
	}
	
	@Override
	public Product getSecondary()
	{
		return getProductID();
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public Arrangement getArrangementID()
	{
		return arrangementID;
	}
	
	public Product getProductID()
	{
		return productID;
	}
	
	public List<ArrangementXProductSecurityToken> getSecurities()
	{
		return securities;
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
		return Objects.hashCode(getId());
	}
}
