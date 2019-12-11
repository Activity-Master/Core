/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.arrangement;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementXArrangementQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IArrangement;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Arrangement",name = "ArrangementXArrangement")
@XmlRootElement

@Access(FIELD)
public class ArrangementXArrangement
		extends WarehouseClassificationRelationshipTable<Arrangement,
						                                                Arrangement,
						                                                ArrangementXArrangement,
				                                                ArrangementXArrangementQueryBuilder,
						                                                Long,
						                                                ArrangementXArrangementSecurityToken,
				                                                IArrangement<?>,
						                                                IArrangement<?>>

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
	protected ArrangementXArrangementSecurityToken configureDefaultsForNewToken(ArrangementXArrangementSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
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
		ArrangementXArrangement that = (ArrangementXArrangement) o;
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
		return getParentArrangementID();
	}

	@Override
	public IArrangement<?> getSecondary()
	{
		return getChildArrangementID();
	}
}
