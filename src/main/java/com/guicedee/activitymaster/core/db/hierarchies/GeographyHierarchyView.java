/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.hierarchies;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseHierarchyView;
import com.guicedee.activitymaster.core.db.hierarchies.builders.GeographyHierarchyViewQueryBuilder;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author Marc Magon
 */
@Entity
@Table(name = "GeographyHierarchyView")
@XmlRootElement

@Immutable
public class GeographyHierarchyView
		extends WarehouseHierarchyView<GeographyHierarchyView, GeographyHierarchyViewQueryBuilder, UUID>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;

	public GeographyHierarchyView()
	{
	}

	public UUID getId()
	{
		return this.id;
	}

	public GeographyHierarchyView setId(UUID id)
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
