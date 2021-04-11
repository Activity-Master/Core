/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.hierarchies;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseHierarchyView;
import com.guicedee.activitymaster.fsdm.db.hierarchies.builders.SecurityHierarchyViewQueryBuilder;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * @author Marc Magon
 */
@Entity
@Table(name = "SecurityHierarchy",
		schema = "Security")
@XmlRootElement
@Immutable
public class SecurityHierarchyView
		extends WarehouseHierarchyView<SecurityHierarchyView, SecurityHierarchyViewQueryBuilder, UUID>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	@OneToMany(mappedBy = "child")
	private List<SecurityHierarchyParents> parents;

	public SecurityHierarchyView()
	{
	}

	@Override
	public UUID getId()
	{
		return id;
	}

	public List<SecurityHierarchyParents> getParents()
	{
		return parents;
	}

	@Override
	public SecurityHierarchyView setId(UUID id)
	{
		this.id = id;
		return this;
	}

	public SecurityHierarchyView setParents(List<SecurityHierarchyParents> parents)
	{
		this.parents = parents;
		return this;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof SecurityHierarchyView))
		{
			return false;
		}
		SecurityHierarchyView other = (SecurityHierarchyView) o;
		if (!other.canEqual((Object) this))
		{
			return false;
		}
		Object this$id = getId();
		Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
		{
			return false;
		}
		return true;
	}

	protected boolean canEqual(Object other)
	{
		return other instanceof SecurityHierarchyView;
	}

	@Override
	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		Object $id = getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}
