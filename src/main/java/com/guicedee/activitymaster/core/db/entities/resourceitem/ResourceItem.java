package com.guicedee.activitymaster.core.db.entities.resourceitem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.entities.address.AddressXResourceItem;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXResourceItem;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConceptXResourceItem;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationXResourceItem;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.events.EventXResourceItem;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXResourceItem;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXResourceItem;
import com.guicedee.activitymaster.core.db.entities.product.ProductXResourceItem;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.db.hierarchies.ResourceItemHierarchyView;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IResourceItem;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;
import static jakarta.persistence.AccessType.*;
import static jakarta.persistence.FetchType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(schema = "Resource",
       name = "ResourceItem")
@XmlRootElement

@Access(FIELD)
public class ResourceItem
		extends WarehouseTable<ResourceItem, ResourceItemQueryBuilder, java.util.UUID, ResourceItemSecurityToken>
		implements IContainsClassifications<ResourceItem, Classification, ResourceItemXClassification, IClassificationValue<?>, IResourceItem<?>, IClassification<?>, ResourceItem>,
		           IContainsResourceItemTypes<ResourceItem, ResourceItemType, ResourceItemXResourceItemType, IResourceType<?>, ResourceItem>,
		           IActivityMasterEntity<ResourceItem>,
		           IContainsActiveFlags<ResourceItem>,
		           IContainsEnterprise<ResourceItem>,
		           IContainsData<ResourceItem>,
		           IContainsHierarchy<ResourceItem, ResourceItemXResourceItem, ResourceItemHierarchyView, IResourceItem<?>,IResourceItem<?>>,
		           IContainsResourceItems<ResourceItem, ResourceItem, ResourceItemXResourceItem, IClassificationValue<?>, IResourceItem<?>, IResourceItem<?>, ResourceItem>,
		           IResourceItem<ResourceItem>
{
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
	        name = "ResourceItemID")
	@JsonValue
	@org.hibernate.annotations.Type(type = "uuid-char")
	private java.util.UUID id;
	@Basic(optional = false,
	       fetch = EAGER)
	@NotNull
	@Size(min = 1,
	      max = 128)
	@Column(nullable = false,
	        length = 128,
	        name = "ResourceItemUUID")
	@JsonIgnore
	private UUID resourceItemUUID;
	@Basic(optional = false,
	       fetch = EAGER)
	@Column(nullable = false,
	        name = "ResourceItemDataType",
	        length = 150)
	@Size(max = 150)
	@JsonIgnore
	private String resourceItemDataType;
	
	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ResourceItemXClassification> classifications;
	
	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ResourceItemXResourceItemType> types;
	
	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<InvolvedPartyXResourceItem> parties;
	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ArrangementXResourceItem> arrangements;
	
	@OneToMany(
			mappedBy = "resource",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ResourceItemData> data;
	
	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<AddressXResourceItem> addresses;
	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventXResourceItem> events;
	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ProductXResourceItem> products;
	
	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ClassificationDataConceptXResourceItem> concept;
	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ClassificationXResourceItem> classificationXResourceItemList;
	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ResourceItemSecurityToken> securities;
	@OneToMany(
			mappedBy = "resourceItemID",
			fetch = FetchType.LAZY)
	@JsonIgnore
	private List<GeographyXResourceItem> geographies;
	
	public ResourceItem()
	{
	
	}
	
	@Override
	protected ResourceItemSecurityToken configureDefaultsForNewToken(ResourceItemSecurityToken stAdmin,  ISystems<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	@Override
	public void configureForClassification(ResourceItemXClassification classificationLink, ISystems<?> system)
	{
		classificationLink.setResourceItemID(this);
	}
	
	public byte[] getData(UUID... identityToken)
	{
		if (data == null || data.isEmpty())
		{
			return new byte[]{};
		}
		Optional<ResourceItemData> d
				= new ResourceItemData().builder()
				                        .inActiveRange(getEnterprise(), identityToken)
				                        .inDateRange()
				                        .where(ResourceItemData_.resource, Equals, this)
				                        .get();
		
		return d.orElseThrow()
		        .getResourceItemData();
	}
	
	@Override
	public void updateData(byte[] data,ISystems<?> system, UUID... identityToken)
	{
		Optional<ResourceItemData> d
				= new ResourceItemData().builder()
				                        .inActiveRange(getEnterprise(), identityToken)
				                        .inDateRange()
				                        .where(ResourceItemData_.resource, Equals, this)
				                        .get();
		if (d.isPresent())
		{
			ResourceItemData rid = d.get();
			rid.setResourceItemData(data);
			rid.updateNow();
		}
		else
		{
			ResourceItemData rid = new ResourceItemData();
			rid.setResource(this);
			rid.setEffectiveFromDate(LocalDateTime.now());
			rid.setWarehouseCreatedTimestamp(LocalDateTime.now());
			rid.setEffectiveToDate(EndOfTime);
			rid.setWarehouseLastUpdatedTimestamp(EndOfTime);
			rid.setResourceItemData(data);
			rid.setSystemID((Systems) system);
			rid.setActiveFlagID(rid.getSystemID().getActiveFlagID());
			rid.setOriginalSourceSystemID((Systems) system);
			rid.setEnterpriseID((Enterprise) system.getEnterpriseID());
			rid.persist();
		}
	}
	
	@Override
	public void updateAndKeepHistoryData(byte[] data,ISystems<?> system, UUID... identityToken)
	{
		Optional<ResourceItemData> d
				= new ResourceItemData().builder()
				                        .inActiveRange(getEnterprise(), identityToken)
				                        .inDateRange()
				                        .where(ResourceItemData_.resource, Equals, this)
				                        .get();
		if (d.isPresent())
		{
			ResourceItemData resourceItemData = d.get();
			resourceItemData.expireIn(Duration.ZERO);
		}
		
		ResourceItemData rid = new ResourceItemData();
		rid.setResource(this);
		rid.setEffectiveFromDate(LocalDateTime.now());
		rid.setWarehouseCreatedTimestamp(LocalDateTime.now());
		rid.setEffectiveToDate(EndOfTime);
		rid.setWarehouseLastUpdatedTimestamp(EndOfTime);
		rid.setResourceItemData(data);
		rid.setActiveFlagID(getActiveFlagID());
		rid.setOriginalSourceSystemID(getSystemID());
		rid.setSystemID(getSystemID());
		rid.setEnterpriseID(getEnterpriseID());
		rid.persist();
		
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
	
	public java.util.UUID getId()
	{
		return this.id;
	}
	
	public UUID getResourceItemUUID()
	{
		return this.resourceItemUUID;
	}
	
	public @Size(max = 150) String getResourceItemDataType()
	{
		return this.resourceItemDataType;
	}
	
	public ResourceItem setId(java.util.UUID id)
	{
		this.id = id;
		return this;
	}
	
	public ResourceItem setResourceItemUUID(UUID resourceItemUUID)
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
		linkTable.setClassificationID((Classification) classificationValue);
	}
	
	@Override
	public void configureNewHierarchyItem(ResourceItemXResourceItem newLink, IResourceItem<?> parent, IResourceItem<?> child, String value)
	{
		newLink.setParentResourceItemID((ResourceItem) parent);
		newLink.setChildResourceItemID((ResourceItem) child);
		newLink.setValue(value);
	}
	
	@Override
	public void configureResourceItemLinkValue(ResourceItemXResourceItem linkTable, ResourceItem primary, ResourceItem secondary, IClassification<?> classificationValue, String value, ISystems<?> system)
	{
		linkTable.setParentResourceItemID(primary);
		linkTable.setChildResourceItemID(secondary);
		linkTable.setClassificationID((Classification) classificationValue);
		linkTable.setValue(value);
		
	}
	
	@Override
	public void configureResourceItemAddable(ResourceItemXResourceItem linkTable, ResourceItem primary, ResourceItem secondary, IClassificationValue<?> classificationValue, String value, ISystems<?> system)
	{
		linkTable.setParentResourceItemID(primary);
		linkTable.setChildResourceItemID(secondary);
		linkTable.setClassificationID((Classification) classificationValue);
		linkTable.setValue(value);
	}
	
}
