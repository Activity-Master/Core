package com.guicedee.activitymaster.fsdm.db.entities.resourceitem;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.logger.LogFactory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.hibernate.AssertionFailure;
import com.guicedee.activitymaster.fsdm.api.Passwords;
import com.guicedee.activitymaster.fsdm.client.services.IPasswordsService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItemType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ResourceItemException;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.AddressXResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConceptXResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationXResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.geography.GeographyXResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductXResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemQueryBuilder;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;

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
		extends WarehouseTable<ResourceItem, ResourceItemQueryBuilder, UUID>
		implements IResourceItem<ResourceItem, ResourceItemQueryBuilder>
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(nullable = false,
	        name = "ResourceItemID")
	@JsonValue
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;
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
		try
		{
			if (data == null || data.isEmpty())
			{
				return null;
			}
		}catch (AssertionFailure a)
		{
			return null;
		}
		
		Optional<ResourceItemData> d
				= new ResourceItemData().builder()
				                        .inActiveRange()
				                        .inDateRange()
				                        .where(ResourceItemData_.resource, Equals, this)
				                        .get();
		if (d.isPresent())
		{
			return unzip(d.get()
			              .getResourceItemData());
		}
		else
		{
			LogFactory.getLog("ResourceItemDataFetch")
			          .log(Level.FINE, "No resource item data exists");
			return new byte[]{};
		}
		
	}
	
	/**
	 * GZip to ensure that the compressed file is always the same given the same input,
	 * zips change headers
	 *
	 * @param data
	 * @return
	 */
	private byte[] zip(byte[] data)
	{
		if ("true".equals(System.getProperty("encrypt", "true")))
		{
			data = new Passwords().integerEncrypt(data).getBytes();
		}
		
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
		     GzipCompressorOutputStream gzipOutput = new GzipCompressorOutputStream(baos))
		{
			gzipOutput.write(data);
			gzipOutput.finish();
			return baos.toByteArray();
		}
		catch (IOException e)
		{
			throw new ResourceItemException("Unable to compress the resource", e);
		}
	}
	
	/**
	 * UnGZIP - maybe add a tarball for password?
	 *
	 * @param data
	 * @return
	 */
	private byte[] unzip(byte[] data)
	{
		try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
		     GzipCompressorInputStream archive = new GzipCompressorInputStream(bais);
		     ByteArrayOutputStream output = new ByteArrayOutputStream())
		{
			IOUtils.copy(archive, output);
			byte[] outcome =output.toByteArray();
			if ("true".equals(System.getProperty("encrypt", "true")))
			{
				outcome = new Passwords().integerDecrypt(new String(outcome));
			}
			return outcome;
		}
		catch (IOException e)
		{
			throw new ResourceItemException("Unable to decompress the resource", e);
		}
	}
	
	//@SuppressWarnings("ResultOfMethodCallIgnored")
	@Override
	public void updateData(byte[] data, ISystems<?, ?> system, UUID... identityToken)
	{
		data = zip(data);
		
		Optional<ResourceItemData> d
				= new ResourceItemData().builder()
				                        .inActiveRange()
				                        .inDateRange()
				                        .where(ResourceItemData_.resource, Equals, this)
				                        .get();
		if (d.isPresent())
		{
			ResourceItemData rid = d.get();
			if (data.length == 0)
			{
				throw new ResourceItemException("Cannot create a resource item that has no data?");
			}
			boolean noUpdate = new ResourceItemData().builder()
			                                         .inActiveRange()
			                                         .inDateRange()
			                                         .where(ResourceItemData_.resourceItemData, Equals, data)
			                                         .where(ResourceItemData_.resource, Equals, this)
			                                         .getCount() > 0;
			if (noUpdate)
			{
				//Identical resource data, no update to occur
				System.out.println("No update required to resource item data");
				return;
			}
			/*rid.getResource();
			rid.getResourceItemData();
			rid.getEnterpriseID();
			rid.getActiveFlagID();
			rid.setResourceItemData(data);
			rid.getOriginalSourceSystemID();*/
			rid.builder()
			   .update(rid);
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
		data = zip(data);
		
		Optional<ResourceItemData> d
				= new ResourceItemData().builder()
				                        .inActiveRange()
				                        .inDateRange()
				                        .where(ResourceItemData_.resource, Equals, this)
				                        .get();
		
		Optional<ResourceItemData> resourceItemData1 = new ResourceItemData().builder()
		                                                                     .inActiveRange()
		                                                                     .inDateRange()
		                                                                     .where(ResourceItemData_.resourceItemData, Equals, data)
		                                                                     .where(ResourceItemData_.resource, Equals, this)
		                                                                     .get();
		if (resourceItemData1.isPresent())
		{
			//Identical resource data, no update to occur
			System.out.println("No update required to resource item data - keep history");
			return;
		}
		
		if (d.isPresent())
		{
			ResourceItemData resourceItemData = d.get();
			resourceItemData.archive();
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
	
	public UUID getId()
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
	
	public ResourceItem setId(UUID id)
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
