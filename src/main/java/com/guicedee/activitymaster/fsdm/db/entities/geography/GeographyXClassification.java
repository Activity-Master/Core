/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.geography;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.geography.builders.GeographyXClassificationQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Geography", name = "GeographyXClassification")
@XmlRootElement

@Access(AccessType.FIELD)
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
		java.lang.String,
		GeographyXClassificationSecurityToken
		>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "GeographyXClassificationID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "GeographyID",
	            referencedColumnName = "GeographyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Geography geographyID;
	
@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
	private List<GeographyXClassificationSecurityToken> securities;
	
	public GeographyXClassification()
	{
	
	}
	
	public GeographyXClassification(java.lang.String geographyXClassificationID)
	{
		this.id = geographyXClassificationID;
	}
	
	@Override
	public void configureSecurityEntity(GeographyXClassificationSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public GeographyXClassification setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public Geography getGeographyID()
	{
		return this.geographyID;
	}
	
	public GeographyXClassification setGeographyID(Geography geographyID)
	{
		this.geographyID = geographyID;
		return this;
	}
	
	public List<GeographyXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
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
