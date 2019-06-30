package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseTable;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConceptXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders.ResourceItemQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.capabilities.*;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IResourceItem;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IResourceType;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static javax.persistence.AccessType.*;
import static javax.persistence.FetchType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "ResourceItem")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ResourceItem
		extends WarehouseTable<ResourceItem, ResourceItemQueryBuilder, Long, ResourceItemSecurityToken>
		implements IContainsClassifications<ResourceItem, Classification, ResourceItemXClassification, IResourceItemClassification<?>, ResourceItem>,
				           IContainsResourceItemTypes<ResourceItem, ResourceItemType, ResourceItemXResourceItemType, IResourceType<?>, ResourceItem>,
				           IActivityMasterEntity<ResourceItem>,
				           IContainsActiveFlags<ResourceItem>,
				           IContainsEnterprise<ResourceItem>,
				           IResourceItem<ResourceItem>
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ResourceItemID")
	private Long id;
	@Basic(optional = false,
			fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 128)
	@Column(nullable = false,
			length = 128,
			name = "ResourceItemUUID")
	private UUID resourceItemUUID;
	@Basic(optional = false,
			fetch = EAGER)
	@Column(nullable = false,
			name = "ResourceItemDataType",
			length = 150)
	@Size(max = 150)
	private String resourceItemDataType;

	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	private List<ResourceItemXClassification> classifications;

	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	private List<ResourceItemXResourceItemType> types;

	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	private List<InvolvedPartyXResourceItem> parties;
	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	private List<ArrangementXResourceItem> arrangements;

	@OneToMany(
			mappedBy = "resource",
			fetch = FetchType.LAZY)
	private List<ResourceItemData> data;

	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	private List<AddressXResourceItem> addresses;
	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	private List<EventXResourceItem> events;
	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	private List<ProductXResourceItem> products;

	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	private List<ClassificationDataConceptXResourceItem> concept;
	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	private List<ClassificationXResourceItem> classificationXResourceItemList;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ResourceItemSecurityToken> securities;
	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	private List<GeographyXResourceItem> geographies;

	public ResourceItem()
	{

	}

	@Override
	protected ResourceItemSecurityToken configureDefaultsForNewToken(ResourceItemSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public void configureForClassification(ResourceItemXClassification classificationLink, IEnterprise<?> enterprise)
	{
		classificationLink.setResourceItemID(this);
	}

	public List<ResourceItemXClassification> getClassifications()
	{
		return this.classifications;
	}

	public List<ResourceItemXResourceItemType> getTypes()
	{
		return this.types;
	}

	public List<InvolvedPartyXResourceItem> getParties()
	{
		return this.parties;
	}

	public List<ArrangementXResourceItem> getArrangements()
	{
		return this.arrangements;
	}

	public List<ResourceItemData> getData()
	{
		return this.data;
	}

	public List<AddressXResourceItem> getAddresses()
	{
		return this.addresses;
	}

	public List<EventXResourceItem> getEvents()
	{
		return this.events;
	}

	public List<ProductXResourceItem> getProducts()
	{
		return this.products;
	}

	public List<ClassificationDataConceptXResourceItem> getConcept()
	{
		return this.concept;
	}

	public List<ClassificationXResourceItem> getClassificationXResourceItemList()
	{
		return this.classificationXResourceItemList;
	}

	public List<ResourceItemSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public List<GeographyXResourceItem> getGeographies()
	{
		return this.geographies;
	}

	public ResourceItem setClassifications(List<ResourceItemXClassification> classifications)
	{
		this.classifications = classifications;
		return this;
	}

	public ResourceItem setTypes(List<ResourceItemXResourceItemType> types)
	{
		this.types = types;
		return this;
	}

	public ResourceItem setParties(List<InvolvedPartyXResourceItem> parties)
	{
		this.parties = parties;
		return this;
	}

	public ResourceItem setArrangements(List<ArrangementXResourceItem> arrangements)
	{
		this.arrangements = arrangements;
		return this;
	}

	public ResourceItem setData(List<ResourceItemData> data)
	{
		this.data = data;
		return this;
	}

	public ResourceItem setAddresses(List<AddressXResourceItem> addresses)
	{
		this.addresses = addresses;
		return this;
	}

	public ResourceItem setEvents(List<EventXResourceItem> events)
	{
		this.events = events;
		return this;
	}

	public ResourceItem setProducts(List<ProductXResourceItem> products)
	{
		this.products = products;
		return this;
	}

	public ResourceItem setConcept(List<ClassificationDataConceptXResourceItem> concept)
	{
		this.concept = concept;
		return this;
	}

	public ResourceItem setClassificationXResourceItemList(List<ClassificationXResourceItem> classificationXResourceItemList)
	{
		this.classificationXResourceItemList = classificationXResourceItemList;
		return this;
	}

	public ResourceItem setSecurities(List<ResourceItemSecurityToken> securities)
	{
		this.securities = securities;
		return this;
	}

	public ResourceItem setGeographies(List<GeographyXResourceItem> geographies)
	{
		this.geographies = geographies;
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
		ResourceItem that = (ResourceItem) o;
		return Objects.equals(getResourceItemUUID(), that.getResourceItemUUID());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getResourceItemUUID());
	}

	@Override
	public String toString()
	{
		return "ResourceItem -  " + getResourceItemUUID();
	}

	public Long getId()
	{
		return this.id;
	}

	public @NotNull @Size(min = 1,
			max = 128) UUID getResourceItemUUID()
	{
		return this.resourceItemUUID;
	}

	public @Size(max = 150) String getResourceItemDataType()
	{
		return this.resourceItemDataType;
	}

	public ResourceItem setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ResourceItem setResourceItemUUID(@NotNull @Size(min = 1,
			max = 128) UUID resourceItemUUID)
	{
		this.resourceItemUUID = resourceItemUUID;
		return this;
	}

	public ResourceItem setResourceItemDataType(@Size(max = 150) String resourceItemDataType)
	{
		this.resourceItemDataType = resourceItemDataType;
		return this;
	}

	@Override
	public void configureResourceItemTypeLinkValue(ResourceItemXResourceItemType linkTable, ResourceItem primary, ResourceItemType secondary, IClassification<?> classificationValue, String value, IEnterprise<?> enterprise)
	{
		linkTable.setResourceItemID(this);
		linkTable.setResourceItemTypeID(secondary);
	}
}
