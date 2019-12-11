/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.hierarchies;

import com.guicedee.activitymaster.core.db.hierarchies.builders.SecurityHierarchyParentsQueryBuilder;
import com.entityassist.BaseEntity;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author Marc Magon
 */
@Entity
@Table(name = "SecurityHierarchyParents")
@XmlRootElement


@Immutable
public class SecurityHierarchyParents
		extends BaseEntity<SecurityHierarchyParents, SecurityHierarchyParentsQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	private Long id;

	@ManyToOne()
	@JoinColumn(name = "id",insertable = false,updatable = false,referencedColumnName = "id")
	private SecurityHierarchyView child;
	@Column(name = "Value")
	private Long value;

	public SecurityHierarchyParents()
	{

	}


	public Long getId()
	{
		return this.id;
	}

	public SecurityHierarchyView getChild()
	{
		return this.child;
	}

	public Long getValue()
	{
		return this.value;
	}

	public SecurityHierarchyParents setId(Long id)
	{
		this.id = id;
		return this;
	}

	public SecurityHierarchyParents setChild(SecurityHierarchyView child)
	{
		this.child = child;
		return this;
	}

	public SecurityHierarchyParents setValue(Long value)
	{
		this.value = value;
		return this;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof SecurityHierarchyParents))
		{
			return false;
		}
		final SecurityHierarchyParents other = (SecurityHierarchyParents) o;
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
		return other instanceof SecurityHierarchyParents;
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
