/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.geography;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.builders.GeographyXClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "GeographyXClassification")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class GeographyXClassification
		extends WarehouseClassificationRelationshipTable<Geography, Classification, GeographyXClassification, GeographyXClassificationQueryBuilder, Long, GeographyXClassificationSecurityToken>
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
	protected GeographyXClassificationSecurityToken configureDefaultsForNewToken(GeographyXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "GeographyXClassification(id=" + this.getId() + ", geographyID=" + this.getGeographyID() + ", securities=" + this.getSecurities() + ")";
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof GeographyXClassification))
		{
			return false;
		}
		final GeographyXClassification other = (GeographyXClassification) o;
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
		return other instanceof GeographyXClassification;
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
