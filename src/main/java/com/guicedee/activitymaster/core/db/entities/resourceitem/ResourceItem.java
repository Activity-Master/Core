package com.guicedee.activitymaster.core.db.entities.resourceitem;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.client.services.builders.warehouse.resourceitem.IResourceItemType;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.core.db.entities.address.AddressXResourceItem;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXResourceItem;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConceptXResourceItem;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationXResourceItem;
import com.guicedee.activitymaster.core.db.entities.events.EventXResourceItem;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXResourceItem;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyXResourceItem;
import com.guicedee.activitymaster.core.db.entities.product.ProductXResourceItem;
import com.guicedee.activitymaster.core.db.entities.resourceitem.builders.ResourceItemQueryBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static com.entityassist.enumerations.Operand.*;
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
@Table(schema = "Resource",
       name = "ResourceItem")
@XmlRootElement

@Access(FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class ResourceItem
		extends WarehouseTable<ResourceItem, ResourceItemQueryBuilder, java.util.UUID>
		implements IResourceItem<ResourceItem, ResourceItemQueryBuilder>
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
		
		ResourceItemData resourceItemData = d.get();
		byte[] resourceItemData1 = resourceItemData.getResourceItemData();
		return resourceItemData1;
	}
	
	@Override
	public void updateData(byte[] data, ISystems<?, ?> system, UUID... identityToken)
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
			rid.builder()
			   .update(rid);
		}
		else
		{
			ResourceItemData rid = new ResourceItemData();
			rid.setResource(this);
			rid.setEffectiveFromDate(StartOfTime);
			rid.setWarehouseCreatedTimestamp(LocalDateTime.now());
			rid.setEffectiveToDate(EndOfTime);
			rid.setWarehouseLastUpdatedTimestamp(EndOfTime);
			rid.setResourceItemData(data);
			rid.setSystemID(system);
			rid.setActiveFlagID(rid.getSystemID()
			                       .getActiveFlagID());
			rid.setOriginalSourceSystemID(system);
			rid.setEnterpriseID(system.getEnterpriseID());
			rid.persist();
		}
	}
	
	@Override
	public void updateAndKeepHistoryData(byte[] data, ISystems<?, ?> system, UUID... identityToken)
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
		rid.setEffectiveFromDate(StartOfTime);
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
		return getResourceItemUUID() + "";
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
	
	//@Override
	public void configureNewHierarchyItem(IWarehouseRelationshipClassificationTable<?, ?, ResourceItem, ResourceItem, UUID> newLink, ResourceItem parent, ResourceItem child, String value)
	{
		ResourceItemXResourceItem ri = (ResourceItemXResourceItem) newLink;
		ri.setParentResourceItemID(parent);
		ri.setChildResourceItemID(child);
		ri.setValue(value);
	}
	
	@Override
	public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable, IClassification<?, ?> classificationValue, ISystems<?, ?> system)
	{
		ResourceItemXClassification rxc = (ResourceItemXClassification) linkTable;
		rxc.setResourceItemID(this);
	}
	
	@Override
	public void configureResourceItemTypeLinkValue(IWarehouseRelationshipTable linkTable, ResourceItem primary, IResourceItemType<?, ?> secondary, IClassification<?, ?> classificationValue, String value, IEnterprise<?, ?> enterprise)
	{
		ResourceItemXResourceItemType rix = (ResourceItemXResourceItemType) linkTable;
		rix.setResourceItemID(primary);
		rix.setResourceItemTypeID((ResourceItemType) secondary);
		rix.setClassificationID(classificationValue);
		rix.setValue(value);
	}
}
