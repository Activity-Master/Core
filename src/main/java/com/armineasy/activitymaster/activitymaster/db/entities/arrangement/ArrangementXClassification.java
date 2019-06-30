/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.arrangement;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementXClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
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
@Table(name = "ArrangementXClassification")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ArrangementXClassification
		extends WarehouseClassificationRelationshipTable<Arrangement, Classification, ArrangementXClassification, ArrangementXClassificationQueryBuilder, Long, ArrangementXClassificationSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ArrangementXClassificationID")
	private Long id;

	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	@JoinColumn(name = "ArrangementID",
			referencedColumnName = "ArrangementID",
			nullable = false)
	private Arrangement arrangementID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ArrangementXClassificationSecurityToken> securities;

	public ArrangementXClassification()
	{

	}

	public ArrangementXClassification(Long arrangementXClassificationID)
	{
		this.id = arrangementXClassificationID;
	}

	@Override
	protected ArrangementXClassificationSecurityToken configureDefaultsForNewToken(ArrangementXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
	{
		return this.id;
	}

	public Arrangement getArrangementID()
	{
		return this.arrangementID;
	}

	public List<ArrangementXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public ArrangementXClassification setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ArrangementXClassification setArrangementID(Arrangement arrangementID)
	{
		this.arrangementID = arrangementID;
		return this;
	}

	public ArrangementXClassification setSecurities(List<ArrangementXClassificationSecurityToken> securities)
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
		if (!(o instanceof ArrangementXClassification))
		{
			return false;
		}
		final ArrangementXClassification other = (ArrangementXClassification) o;
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
		return other instanceof ArrangementXClassification;
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
