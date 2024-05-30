/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.geography;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.geography.builders.GeographyXResourceItemQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
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
@Table(schema = "Geography", name = "GeographyXResourceItem")
@XmlRootElement

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class GeographyXResourceItem
		extends WarehouseClassificationRelationshipTable<Geography,
		ResourceItem,
		GeographyXResourceItem,
		GeographyXResourceItemQueryBuilder,
		java.lang.String,
		GeographyXResourceItemSecurityToken
		>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "GeographyXResourceItemID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<GeographyXResourceItemSecurityToken> securities;
	
	@JoinColumn(name = "GeographyID",
	            referencedColumnName = "GeographyID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Geography geographyID;
	@JoinColumn(name = "ResourceItemID",
	            referencedColumnName = "ResourceItemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private ResourceItem resourceItemID;
	
	public GeographyXResourceItem()
	{
	
	}
	
	public GeographyXResourceItem(java.lang.String geographyXResourceItemID)
	{
		this.id = geographyXResourceItemID;
	}
	
	@Override
	public void configureSecurityEntity(GeographyXResourceItemSecurityToken securityEntity)
	{
		securityEntity.setBase(this);
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public GeographyXResourceItem setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public List<GeographyXResourceItemSecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public GeographyXResourceItem setSecurities(List<GeographyXResourceItemSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public Geography getGeographyID()
	{
		return this.geographyID;
	}
	
	public GeographyXResourceItem setGeographyID(Geography geographyID)
	{
		this.geographyID = geographyID;
		return this;
	}
	
	public ResourceItem getResourceItemID()
	{
		return this.resourceItemID;
	}
	
	public GeographyXResourceItem setResourceItemID(ResourceItem resourceItemID)
	{
		this.resourceItemID = resourceItemID;
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
		GeographyXResourceItem that = (GeographyXResourceItem) o;
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
	public ResourceItem getSecondary()
	{
		return getResourceItemID();
	}
}
