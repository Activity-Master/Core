/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.geography;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.geography.builders.GeographyXClassificationQueryBuilder;
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
@Table(schema="Geography",name = "GeographyXClassification")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class GeographyXClassification
		extends WarehouseClassificationRelationshipTable<Geography,
				                                                Classification,
				                                                GeographyXClassification,
				                                                GeographyXClassificationQueryBuilder,
				                                                java.util.UUID>
		implements Serializable
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
			name = "GeographyXClassificationID")@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;

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

	public GeographyXClassification(UUID geographyXClassificationID)
	{
		this.id = geographyXClassificationID;
	}
	

	public java.util.UUID getId()
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

	public GeographyXClassification setId(java.util.UUID id)
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
	public Geography getPrimary()
	{
		return getGeographyID();
	}

	@Override
	public Classification getSecondary()
	{
		return getClassificationID();
	}
}
