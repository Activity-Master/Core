/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.geography;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.geography.builders.GeographyXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IGeography;
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
@Table(schema="Geography",name = "GeographyXClassification")
@XmlRootElement

@Access(FIELD)
public class GeographyXClassification
		extends WarehouseClassificationRelationshipTable<Geography,
				                                                Classification,
				                                                GeographyXClassification,
				                                                GeographyXClassificationQueryBuilder,
				                                                Long,
				                                                GeographyXClassificationSecurityToken,
				                                                IGeography<?>, IClassification<?>>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "GeographyXClassificationID")
	private Long id;

	@JoinColumn(name = "GeographyID",
			referencedColumnName = "GeographyID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Geography geographyID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<GeographyXClassificationSecurityToken> securities;

	public GeographyXClassification()
	{

	}

	public GeographyXClassification(Long geographyXClassificationID)
	{
		this.id = geographyXClassificationID;
	}

	@Override
	protected GeographyXClassificationSecurityToken configureDefaultsForNewToken(GeographyXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
	{
		return this.id;
	}

	public Geography getGeographyID()
	{
		return this.geographyID;
	}

	public List<GeographyXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public GeographyXClassification setId(Long id)
	{
		this.id = id;
		return this;
	}

	public GeographyXClassification setGeographyID(Geography geographyID)
	{
		this.geographyID = geographyID;
		return this;
	}

	public GeographyXClassification setSecurities(List<GeographyXClassificationSecurityToken> securities)
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
		GeographyXClassification that = (GeographyXClassification) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IGeography<?> getPrimary()
	{
		return getGeographyID();
	}

	@Override
	public IClassification<?> getSecondary()
	{
		return getClassificationID();
	}
}
