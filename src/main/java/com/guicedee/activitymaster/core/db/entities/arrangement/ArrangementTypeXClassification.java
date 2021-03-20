/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.arrangement;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementTypeXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;

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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ArrangementTypeXClassification
		extends WarehouseClassificationRelationshipTable<ArrangementType,
		Classification,
		ArrangementTypeXClassification,
		ArrangementTypeXClassificationQueryBuilder,
		java.util.UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementTypeXClassificationID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
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
	
	public ArrangementTypeXClassification(UUID arrangementXClassificationID)
	{
		this.id = arrangementXClassificationID;
	}
	
	
	@Override
	public java.util.UUID getId()
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
	public ArrangementTypeXClassification setId(java.util.UUID id)
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
	public ArrangementType getPrimary()
	{
		return getArrangementTypeID();
	}
	
	@Override
	public Classification getSecondary()
	{
		return getClassificationID();
	}
}
