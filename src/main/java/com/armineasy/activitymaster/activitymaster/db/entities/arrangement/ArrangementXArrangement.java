/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.arrangement;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementXArrangementQueryBuilder;
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
@Table(name = "ArrangementXArrangement")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ArrangementXArrangement
		extends WarehouseClassificationRelationshipTable<Arrangement, Arrangement, ArrangementXArrangement, ArrangementXArrangementQueryBuilder, Long, ArrangementXArrangementSecurityToken>

{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ArrangementXArrangementID")
	private Long id;

	@JoinColumn(name = "ChildArrangementID",
			referencedColumnName = "ArrangementID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Arrangement childArrangementID;
	@JoinColumn(name = "ParentArrangementID",
			referencedColumnName = "ArrangementID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Arrangement parentArrangementID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementXArrangementSecurityToken> securities;

	public ArrangementXArrangement()
	{

	}

	public ArrangementXArrangement(Long arrangementXArrangementID)
	{
		this.id = arrangementXArrangementID;
	}

	@Override
	protected ArrangementXArrangementSecurityToken configureDefaultsForNewToken(ArrangementXArrangementSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "ArrangementXArrangement(id=" + this.getId() + ", childArrangementID=" + this.getChildArrangementID() + ", parentArrangementID=" + this.getParentArrangementID() +
		       ", securities=" + this.getSecurities() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public Arrangement getChildArrangementID()
	{
		return this.childArrangementID;
	}

	public Arrangement getParentArrangementID()
	{
		return this.parentArrangementID;
	}

	public List<ArrangementXArrangementSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public ArrangementXArrangement setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ArrangementXArrangement setChildArrangementID(Arrangement childArrangementID)
	{
		this.childArrangementID = childArrangementID;
		return this;
	}

	public ArrangementXArrangement setParentArrangementID(Arrangement parentArrangementID)
	{
		this.parentArrangementID = parentArrangementID;
		return this;
	}

	public ArrangementXArrangement setSecurities(List<ArrangementXArrangementSecurityToken> securities)
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
		if (!(o instanceof ArrangementXArrangement))
		{
			return false;
		}
		final ArrangementXArrangement other = (ArrangementXArrangement) o;
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
		return other instanceof ArrangementXArrangement;
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
