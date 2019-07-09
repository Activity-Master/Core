/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.geography;

import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXGeography;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.builders.GeographyQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsResourceItems;
import com.armineasy.activitymaster.activitymaster.services.classifications.geography.IGeographyClassification;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.*;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IResourceType;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;
import static javax.persistence.FetchType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "Geography")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class Geography
		extends WarehouseSCDNameDescriptionTable<Geography, GeographyQueryBuilder, Long, GeographySecurityToken>
		implements IContainsClassifications<Geography, Classification, GeographyXClassification, IGeographyClassification<?>,IGeography<?>,IClassification<?>, Geography>,
				           IContainsResourceItems<Geography, ResourceItem, GeographyXResourceItem, IResourceItemClassification<?>,IGeography<?>, IResourceItem<?>,Geography>,
				           IActivityMasterEntity<Geography>,
				           IGeography<Geography>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "GeographyID")
	private Long id;

	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 500)
	@Column(nullable = false,
			length = 500,
			name = "GeographyName")
	private String name;
	@Basic(optional = false,fetch = EAGER)
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

	public Geography(Long geographyID)
	{
		this.id = geographyID;
	}

	public Geography(Long geographyID, String geographyName, String geographyDesc)
	{
		this.id = geographyID;
		this.name = geographyName;
		this.description = geographyDesc;

	}

	@Override
	protected GeographySecurityToken configureDefaultsForNewToken(GeographySecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public void configureForClassification(GeographyXClassification classificationLink, IEnterprise<?> enterprise)
	{
		classificationLink.setGeographyID(this);
	}

	public void configureNewHierarchyItem(GeographyXGeography newLink, Geography parent, Geography child, String value)
	{
		newLink.setParentGeographyID(parent);
		newLink.setChildGeographyID(child);
		if (value != null)
		{
			newLink.setValue(value);
		}
	}

	@Override
	public void configureResourceItemLinkValue(GeographyXResourceItem linkTable, Geography primary, ResourceItem secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setGeographyID(this);
		linkTable.setResourceItemID(secondary);
	}

	public List<GeographyXClassification> getClassifications()
	{
		return this.classifications;
	}

	public List<AddressXGeography> getAddresses()
	{
		return this.addresses;
	}

	public Classification getClassificationID()
	{
		return this.classificationID;
	}

	public List<GeographySecurityToken> getSecurities()
	{
		return this.securities;
	}

	public List<GeographyXResourceItem> getResources()
	{
		return this.resources;
	}

	public List<GeographyXGeography> getGeographyXGeographyList()
	{
		return this.geographyXGeographyList;
	}

	public List<GeographyXGeography> getGeographyXGeographyList1()
	{
		return this.geographyXGeographyList1;
	}

	public Geography setClassifications(List<GeographyXClassification> classifications)
	{
		this.classifications = classifications;
		return this;
	}

	public Geography setAddresses(List<AddressXGeography> addresses)
	{
		this.addresses = addresses;
		return this;
	}

	public Geography setClassificationID(Classification classificationID)
	{
		this.classificationID = classificationID;
		return this;
	}

	public Geography setSecurities(List<GeographySecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public Geography setResources(List<GeographyXResourceItem> resources)
	{
		this.resources = resources;
		return this;
	}

	public Geography setGeographyXGeographyList(List<GeographyXGeography> geographyXGeographyList)
	{
		this.geographyXGeographyList = geographyXGeographyList;
		return this;
	}

	public Geography setGeographyXGeographyList1(List<GeographyXGeography> geographyXGeographyList1)
	{
		this.geographyXGeographyList1 = geographyXGeographyList1;
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
		Geography geography = (Geography) o;
		return Objects.equals(getId(), geography.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public String toString()
	{
		return "Geography - " + getName();
	}

	public Long getId()
	{
		return this.id;
	}

	public @NotNull @Size(min = 1,
			max = 500) String getName()
	{
		return this.name;
	}

	public @NotNull @Size(min = 1,
			max = 500) String getDescription()
	{
		return this.description;
	}

	public Geography setId(Long id)
	{
		this.id = id;
		return this;
	}

	public Geography setName(@NotNull @Size(min = 1,
			max = 500) String name)
	{
		this.name = name;
		return this;
	}

	public Geography setDescription(@NotNull @Size(min = 1,
			max = 500) String description)
	{
		this.description = description;
		return this;
	}

}
