package com.guicedee.activitymaster.fsdm.db.entities.resourceitem;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.AddressXResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConceptXResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationXResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.geography.GeographyXResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductXResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemQueryBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.extern.java.Log;

import java.io.Serial;
import java.util.*;
import java.util.logging.Level;

import static com.entityassist.enumerations.Operand.*;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
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

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Log
public class ResourceItem
		extends WarehouseSCDTable<ResourceItem, ResourceItemQueryBuilder, String, ResourceItemSecurityToken>
		implements IResourceItem<ResourceItem, ResourceItemQueryBuilder>
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(nullable = false,
	        name = "ResourceItemID")
	@JsonValue
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private String id;
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
	public IResourceItem<?, ?> updateDataTypeValue(String newValue)
	{
		setResourceItemDataType(newValue);
		builder().find(getId())
		         .update(this);
		return this;
	}
	
	public byte[] getData(java.util.UUID... identityToken)
	{
		var dr = getDataRow();
		Optional<byte[]> d
				= new ResourceItemData().builder()
				                        .inActiveRange()
				                        .inDateRange()
				                        .where(ResourceItemData_.resource, Equals, this)
				                        .selectColumn(ResourceItemData_.resourceItemData)
				                        .get(byte[].class);
		if (d.isPresent())
		{
			byte[] data = d.get();
			//	byte[] data = (byte[]) dataObject[0];
			return unzip(data);
		}
		else
		{
			log.log(Level.FINE, "No resource item data exists");
			return new byte[]{};
		}
	}
	
	@Override
	public String getFilename()
	{
		var dr = getDataRow();
		if (dr.isPresent())
		{
			ResourceItemData r = (ResourceItemData) dr.get();
			var id = r.getId();
			return "data/" + id + ".dat";
		}
		return null;
	}
	
	@Override
	public Optional<IResourceData<?, ?, ?>> getDataRow(UUID... identityToken)
	{
		return (Optional) new ResourceItemData().builder()
		                                        .inActiveRange()
		                                        .inDateRange()
		                                        .where(ResourceItemData_.resource, Equals, this)
		                                        .get();
	}
	
	/**
	 * GZip to ensure that the compressed file is always the same given the same input,
	 * zips change headers
	 *
	 * @param data
	 * @return
	 */
	byte[] zip(byte[] data)
	{
		return data;
	}
	
	/**
	 * UnGZIP - maybe add a tarball for password?
	 *
	 * @param data
	 * @return
	 */
	byte[] unzip(byte[] data)
	{
		byte[] outcome = data;
		return outcome;
	}
	
	
	//@SuppressWarnings("ResultOfMethodCallIgnored")
	@Override
	public void updateData(byte[] data, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		//	System.out.println(LocalDateTime.now() + " start zip - " + data.length);
		if (data == null || data.length == 0)
		{
			throw new RuntimeException("Cannot store 0 data into a resource item");
		}
		//	System.out.println(LocalDateTime.now() + " start search");
		Optional<ResourceItemData> d
				= new ResourceItemData().builder()
				                        .inActiveRange()
				                        //    .inDateRange()
				                        .where(ResourceItemData_.resource, Equals, this)
				                        .latestFirst()
				                        .setReturnFirst(true)
				                        .get();
		if (d.isPresent())
		{
			ResourceItemData rid = d.get();
			rid.setResourceItemData(data);
			rid.update();
		}
		
	}
	
	@Override
	public void updateAndKeepHistoryData(byte[] data, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		data = zip(data);
		
		Optional<ResourceItemData> d
				= new ResourceItemData().builder()
				                        .inActiveRange()
				                        // .inDateRange()
				                        .where(ResourceItemData_.resource, Equals, this)
				                        .latestFirst()
				                        .setReturnFirst(true)
				                        .get();
		
		if (d.isPresent())
		{
			ResourceItemData resourceItemData = d.get();
			resourceItemData.archive();
		}
		
		ResourceItemData rid = new ResourceItemData();
		rid.setResource(this);
		rid.setEffectiveFromDate(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		rid.setWarehouseCreatedTimestamp(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		rid.setEffectiveToDate(EndOfTime.atOffset(java.time.ZoneOffset.UTC));
		rid.setWarehouseLastUpdatedTimestamp(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		rid.setResourceItemData(data);
		rid.setActiveFlagID(getActiveFlagID());
		rid.setOriginalSourceSystemID(getSystemID());
		rid.setSystemID(getSystemID());
		rid.setEnterpriseID(system.getEnterpriseID());
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
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}
	
	@Override
	public String toString()
	{
		return getId() + "";
	}
	
	public java.lang.String getId()
	{
		return this.id;
	}
	
	public ResourceItem setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public @Size(max = 150) String getResourceItemDataType()
	{
		return this.resourceItemDataType;
	}
	
	public ResourceItem setResourceItemDataType(@Size(max = 150) String resourceItemDataType)
	{
		this.resourceItemDataType = resourceItemDataType;
		return this;
	}
	
	@Override
	public void configureNewHierarchyItem(IWarehouseRelationshipClassificationTable<?, ?, ResourceItem, ResourceItem, java.lang.String, ?> newLink, ResourceItem parent, ResourceItem child, String value)
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
