/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.hierarchies;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseHierarchyView;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.builders.GeographyHierarchyViewQueryBuilder;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author GedMarc
 */
@Entity
@Table(name = "GeographyHierarchyView")
@XmlRootElement
@Accessors(chain = true)
@Immutable
public class GeographyHierarchyView
		extends WarehouseHierarchyView<GeographyHierarchyView, GeographyHierarchyViewQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	private Long id;

	public GeographyHierarchyView()
	{
	}

	public Long getId()
	{
		return this.id;
	}

	public GeographyHierarchyView setId(Long id)
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
		if (!(o instanceof GeographyHierarchyView))
		{
			return false;
		}
		final GeographyHierarchyView other = (GeographyHierarchyView) o;
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
		return other instanceof GeographyHierarchyView;
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
