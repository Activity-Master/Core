/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.geography;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.geography.builders.GeographyXGeographyQueryBuilder;
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
@Table(schema = "Geography", name = "GeographyXGeography")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class GeographyXGeography
		extends WarehouseClassificationRelationshipTable<Geography,
		Geography,
		GeographyXGeography,
		GeographyXGeographyQueryBuilder,
		UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "GeographyXGeographyID")
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<GeographyXGeographySecurityToken> securities;
	
	@JoinColumn(name = "ParentGeographyID",
	            referencedColumnName = "GeographyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Geography parentGeographyID;
	@JoinColumn(name = "ChildGeographyID",
	            referencedColumnName = "GeographyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Geography childGeographyID;
	
	public GeographyXGeography()
	{
	
	}
	
	public GeographyXGeography(UUID geographyXGeographyID)
	{
		this.id = geographyXGeographyID;
	}
	
	public UUID getId()
	{
		return this.id;
	}
	
	public GeographyXGeography setId(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public List<GeographyXGeographySecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public GeographyXGeography setSecurities(List<GeographyXGeographySecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public Geography getParentGeographyID()
	{
		return this.parentGeographyID;
	}
	
	public GeographyXGeography setParentGeographyID(Geography parentGeographyID)
	{
		this.parentGeographyID = parentGeographyID;
		return this;
	}
	
	public Geography getChildGeographyID()
	{
		return this.childGeographyID;
	}
	
	public GeographyXGeography setChildGeographyID(Geography childGeographyID)
	{
		this.childGeographyID = childGeographyID;
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
		GeographyXGeography that = (GeographyXGeography) o;
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
		return getParentGeographyID();
	}
	
	@Override
	public Geography getSecondary()
	{
		return getChildGeographyID();
	}
}
