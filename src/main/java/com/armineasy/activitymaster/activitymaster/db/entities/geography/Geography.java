/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.geography;

import com.armineasy.activitymaster.activitymaster.db.abstraction.assists.WarehouseSCDNameDescriptionTable;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXGeography;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConcept;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.builders.GeographyQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsResourceItems;
import com.armineasy.activitymaster.activitymaster.services.classifications.geography.IGeographyClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static javax.persistence.AccessType.*;

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
@EqualsAndHashCode(of = "id",
		callSuper = false)
@Access(FIELD)@lombok.Data
public class Geography
		extends WarehouseSCDNameDescriptionTable<Geography, GeographyQueryBuilder, Long, GeographySecurityToken>
		implements IContainsClassifications<Geography, Classification, GeographyXClassification, IGeographyClassification<?>,Geography>,
				           IContainsResourceItems<Geography, ResourceItem, GeographyXResourceItem>,
				           IActivityMasterEntity<Geography>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "GeographyID")
	@Getter
	@Setter
	private Long id;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 500)
	@Column(nullable = false,
			length = 500,
			name = "GeographyName")
	@Getter
	@Setter
	private String name;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 500)
	@Column(nullable = false,
			length = 500,
			name = "GeographyDesc")
	@Getter
	@Setter
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
	public void setMyResourceItemLinkValue(GeographyXResourceItem classificationLink, ResourceItem resourceItem, IEnterprise<?> enterprise)
	{
		classificationLink.setGeographyID(this);
		classificationLink.setResourceItemID(resourceItem);
	}
}
