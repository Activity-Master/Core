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

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.AccessType.*;

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
						                                                java.util.UUID,
						                                                ArrangementXArrangementSecurityToken,
				                                                IArrangement<?>,
						                                                IArrangement<?>>

{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
			name = "ArrangementXArrangementID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

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

	public ArrangementXArrangement(UUID arrangementXArrangementID)
	{
		this.id = arrangementXArrangementID;
	}

	@Override
	protected ArrangementXArrangementSecurityToken configureDefaultsForNewToken(ArrangementXArrangementSecurityToken stAdmin,  ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public java.util.UUID getId()
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

	public ArrangementXArrangement setId(java.util.UUID id)
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
