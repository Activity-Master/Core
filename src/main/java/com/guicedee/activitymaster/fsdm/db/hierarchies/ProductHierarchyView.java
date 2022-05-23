/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.hierarchies;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseHierarchyView;
import com.guicedee.activitymaster.fsdm.db.hierarchies.builders.ProductHierarchyViewQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Immutable;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Marc Magon
 */
@Entity
@Table(name = "ProductHierarchyView")
@XmlRootElement

@Immutable
public class ProductHierarchyView
		extends WarehouseHierarchyView<ProductHierarchyView, ProductHierarchyViewQueryBuilder, java.lang.String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	
	public ProductHierarchyView()
	{
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public ProductHierarchyView setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof ProductHierarchyView))
		{
			return false;
		}
		final ProductHierarchyView other = (ProductHierarchyView) o;
		if (!other.canEqual(this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		return this$id == null ? other$id == null : this$id.equals(other$id);
	}
	
	protected boolean canEqual(final Object other)
	{
		return other instanceof ProductHierarchyView;
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
