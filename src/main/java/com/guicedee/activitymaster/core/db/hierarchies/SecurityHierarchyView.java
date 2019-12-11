/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.hierarchies;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseHierarchyView;
import com.guicedee.activitymaster.core.db.hierarchies.builders.SecurityHierarchyViewQueryBuilder;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * @author Marc Magon
 */
@Entity
@Table(name = "SecurityHierarchy",
		schema = "Security")
@XmlRootElement
@Immutable
public class SecurityHierarchyView
		extends WarehouseHierarchyView<SecurityHierarchyView, SecurityHierarchyViewQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	@OneToMany(mappedBy = "child")
	private List<SecurityHierarchyParents> parents;

	public SecurityHierarchyView()
	{
	}

	@Override
	public Long getId()
	{
		return id;
	}

	public List<SecurityHierarchyParents> getParents()
	{
		return parents;
	}

	@Override
	public SecurityHierarchyView setId(Long id)
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
