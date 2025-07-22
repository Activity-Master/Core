/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementTypeXClassificationQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Arrangement",
       name = "ArrangementTypeXClassification")
@XmlRootElement
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArrangementTypeXClassification
		extends WarehouseClassificationRelationshipTable<ArrangementType,
                                Classification,
                                ArrangementTypeXClassification,
                                ArrangementTypeXClassificationQueryBuilder,
                                UUID,
                                ArrangementTypeXClassificationSecurityToken
                                >
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementTypeXClassificationID")
	
	private java.util.UUID id;
	
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	@JoinColumn(name = "ArrangementTypeID",
	            referencedColumnName = "ArrangementTypeID",
	            nullable = false)
	
	private ArrangementType arrangementID;
	
@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<ArrangementTypeXClassificationSecurityToken> securities;

	@Override
	public void configureSecurityEntity(ArrangementTypeXClassificationSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	public ArrangementType getArrangementTypeID()
	{
		return this.arrangementID;
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
	public ArrangementType getPrimary()
	{
		return getArrangementTypeID();
	}
	
	@Override
	public Classification getSecondary()
	{
		return getClassificationID();
	}
	
	public List<ArrangementTypeXClassificationSecurityToken> getSecurities()
	{
		return securities;
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
		return Objects.hashCode(getId());
	}
}
