/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.arrangement;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementTypeXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.services.dto.IArrangementType;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Arrangement",
       name = "ArrangementTypeXClassification")
@XmlRootElement
@Access(FIELD)
public class ArrangementTypeXClassification
		extends WarehouseClassificationRelationshipTable<ArrangementType,
		Classification,
		ArrangementTypeXClassification,
		ArrangementTypeXClassificationQueryBuilder,
		Long,
		ArrangementTypeXClassificationSecurityToken,
		IArrangementType<?>, IClassification<?>>
		implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
	        name = "ArrangementTypeXClassificationID")
	private Long id;
	
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	@JoinColumn(name = "ArrangementTypeID",
	            referencedColumnName = "ArrangementTypeID",
	            nullable = false)
	private ArrangementType arrangementID;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementTypeXClassificationSecurityToken> securities;
	
	public ArrangementTypeXClassification()
	{
	
	}
	
	public ArrangementTypeXClassification(Long arrangementXClassificationID)
	{
		this.id = arrangementXClassificationID;
	}
	
	@Override
	protected ArrangementTypeXClassificationSecurityToken configureDefaultsForNewToken(ArrangementTypeXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	@Override
	public Long getId()
	{
		return this.id;
	}
	
	public ArrangementType getArrangementTypeID()
	{
		return this.arrangementID;
	}
	
	public List<ArrangementTypeXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	@Override
	public ArrangementTypeXClassification setId(Long id)
	{
		this.id = id;
		return this;
	}
	
	public ArrangementTypeXClassification setArrangementTypeID(ArrangementType arrangementID)
	{
		this.arrangementID = arrangementID;
		return this;
	}
	
	public ArrangementTypeXClassification setSecurities(List<ArrangementTypeXClassificationSecurityToken> securities)
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
		ArrangementTypeXClassification that = (ArrangementTypeXClassification) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public IArrangementType<?> getPrimary()
	{
		return getArrangementTypeID();
	}
	
	@Override
	public IClassification<?> getSecondary()
	{
		return getClassificationID();
	}
}
