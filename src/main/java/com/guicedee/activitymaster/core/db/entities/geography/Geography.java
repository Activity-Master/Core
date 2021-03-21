/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.geography;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.client.services.builders.warehouse.geography.IGeography;
import com.guicedee.activitymaster.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.core.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.guicedee.activitymaster.core.db.entities.address.AddressXGeography;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.geography.builders.GeographyQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.AccessType.*;
import static jakarta.persistence.FetchType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings({"unused", "rawtypes"})
@Entity
@Table(schema = "Geography",
       name = "Geography")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class Geography
		extends WarehouseSCDNameDescriptionTable<Geography, GeographyQueryBuilder, java.util.UUID>
		implements IGeography<Geography, GeographyQueryBuilder>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "GeographyID")
	@JsonValue
	@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 500)
	@Column(nullable = false,
	        length = 500,
	        name = "GeographyName")
	private String name;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 500)
	@Column(nullable = false,
	        length = 500,
	        name = "GeographyDesc")
	private String description;
	
	@OneToMany(
			mappedBy = "geographyID",
			fetch = FetchType.LAZY)
	private List<GeographyXClassification> classifications;
	
	@OneToMany(
			mappedBy = "geographyID",
			fetch = FetchType.LAZY)
	private List<AddressXGeography> addresses;
	
	@JoinColumn(name = "ClassificationID",
	            referencedColumnName = "ClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private Classification classificationID;
	
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<GeographySecurityToken> securities;
	
	@OneToMany(
			mappedBy = "geographyID",
			fetch = FetchType.LAZY)
	private List<GeographyXResourceItem> resources;
	@OneToMany(
			mappedBy = "parentGeographyID",
			fetch = FetchType.LAZY)
	private List<GeographyXGeography> geographyXGeographyList;
	@OneToMany(
			mappedBy = "childGeographyID",
			fetch = FetchType.LAZY)
	private List<GeographyXGeography> geographyXGeographyList1;
	
	public Geography()
	{
	
	}
	
	public Geography(UUID geographyID)
	{
		this.id = geographyID;
	}
	
	public Geography(UUID geographyID, String geographyName, String geographyDesc)
	{
		this.id = geographyID;
		this.name = geographyName;
		this.description = geographyDesc;
		
	}
	
	public List<GeographyXClassification> getClassifications()
	{
		return this.classifications;
	}
	
	public Geography setClassifications(List<GeographyXClassification> classifications)
	{
		this.classifications = classifications;
		return this;
	}
	
	public List<AddressXGeography> getAddresses()
	{
		return this.addresses;
	}
	
	public Geography setAddresses(List<AddressXGeography> addresses)
	{
		this.addresses = addresses;
		return this;
	}
	
	public Classification getClassificationID()
	{
		return this.classificationID;
	}
	
	@Override
	public Geography setClassificationID(IClassification<?, ?> classificationID)
	{
		this.classificationID = (Classification) classificationID;
		return this;
	}
	
	public Geography setClassificationID(Classification classificationID)
	{
		this.classificationID = classificationID;
		return this;
	}
	
	public List<GeographySecurityToken> getSecurities()
	{
		return this.securities;
	}
	
	public Geography setSecurities(List<GeographySecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}
	
	public List<GeographyXResourceItem> getResources()
	{
		return this.resources;
	}
	
	public Geography setResources(List<GeographyXResourceItem> resources)
	{
		this.resources = resources;
		return this;
	}
	
	public List<GeographyXGeography> getGeographyXGeographyList()
	{
		return this.geographyXGeographyList;
	}
	
	public Geography setGeographyXGeographyList(List<GeographyXGeography> geographyXGeographyList)
	{
		this.geographyXGeographyList = geographyXGeographyList;
		return this;
	}
	
	public List<GeographyXGeography> getGeographyXGeographyList1()
	{
		return this.geographyXGeographyList1;
	}
	
	public Geography setGeographyXGeographyList1(List<GeographyXGeography> geographyXGeographyList1)
	{
		this.geographyXGeographyList1 = geographyXGeographyList1;
		return this;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
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
		Geography geography = (Geography) o;
		return Objects.equals(getId(), geography.getId());
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public Geography setName(@NotNull @Size(min = 1,
	                                        max = 500) String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	@Override
	public Geography setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public Geography setDescription(@NotNull @Size(min = 1,
	                                               max = 500) String description)
	{
		this.description = description;
		return this;
	}
	
	public Geography setClassification(Classification classification)
	{
		this.classificationID = classification;
		return this;
	}
	
	@Override
	public void configureNewHierarchyItem(IWarehouseRelationshipClassificationTable<?, ?, Geography, Geography, UUID> newLink, Geography parent, Geography child, String value)
	{
		GeographyXGeography g = (GeographyXGeography) newLink;
		g.setParentGeographyID(parent);
		g.setChildGeographyID(child);
		g.setValue(value);
		
	}
	
	@Override
	public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable,IClassification<?,?> classificationValue, ISystems<?, ?> system)
	{
		((GeographyXClassification) linkTable).setGeographyID(this);
	}
	
	@Override
	public void configureResourceItemAddable(IWarehouseRelationshipTable linkTable, Geography primary, IResourceItem<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
	{
		GeographyXResourceItem g = (GeographyXResourceItem) linkTable;
		g.setGeographyID(primary);
		g.setResourceItemID((ResourceItem) secondary);
		g.setClassificationID(classificationValue);
		g.setValue(value);
	}
}
