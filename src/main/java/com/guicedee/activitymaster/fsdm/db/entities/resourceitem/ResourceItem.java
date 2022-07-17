package com.guicedee.activitymaster.fsdm.db.entities.resourceitem;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.api.Passwords;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.*;
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
import com.guicedee.logger.LogFactory;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

import static com.entityassist.enumerations.Operand.*;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.*;
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
public class ResourceItem
		extends WarehouseTable<ResourceItem, ResourceItemQueryBuilder, java.lang.String>
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
		if (flushToDisk)
		{
			if (dr.isPresent())
			{
				File searchFile = new File("data/" + dr.get()
				                                       .getId() + ".dat");
				if (searchFile.exists())
				{
					if (flushExploded)
					{
						File explodedFile = new File("data/" + dr.get()
						                                         .getId() + ".exploded");
						if (explodedFile.exists())
						{
							try (FileInputStream fis = new FileInputStream(explodedFile);
							     BufferedInputStream bis = new BufferedInputStream(fis))
							{
								byte[] data = bis.readAllBytes();
								return data;
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							try (FileInputStream fis = new FileInputStream(searchFile);
							     BufferedInputStream bis = new BufferedInputStream(fis))
							{
								byte[] data = bis.readAllBytes();
								data = unzip(data);
								if (explodedFile.createNewFile())
								{
									try (FileOutputStream fos = new FileOutputStream(explodedFile);
									     BufferedOutputStream bos = new BufferedOutputStream(fos))
									{
										bos.write(data);
									}
								}
								return data;
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					else
					{
						try (FileInputStream fis = new FileInputStream(searchFile);
						     BufferedInputStream bis = new BufferedInputStream((fis)))
						{
							byte[] data = bis.readAllBytes();
							return unzip(data);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				else
				{
					try
					{
						//	System.out.println("Cannot find dat File - " + searchFile.getCanonicalPath());
						if (flushExploded)
						{
							File explodedFile = new File("data/" + dr.get()
							                                         .getId() + ".exploded");
							if (explodedFile.exists())
							{
								try (FileInputStream fis = new FileInputStream(explodedFile);
								     BufferedInputStream bis = new BufferedInputStream(fis))
								{
									byte[] data = bis.readAllBytes();
									return data;
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}
							else
							{
								try (FileInputStream fis = new FileInputStream(searchFile);
								     BufferedInputStream bis = new BufferedInputStream(fis))
								{
									byte[] data = bis.readAllBytes();
									data = unzip(data);
									if (explodedFile.createNewFile())
									{
										try (FileOutputStream fos = new FileOutputStream(explodedFile);
										     BufferedOutputStream bos = new BufferedOutputStream(fos))
										{
											bos.write(data);
											return data;
										}
									}
									return data;
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		
		Optional<Object[]> d
				= new ResourceItemData().builder()
				                        .inActiveRange()
				                        .inDateRange()
				                        .where(ResourceItemData_.resource, Equals, this)
				                        .selectColumn(ResourceItemData_.resourceItemData)
				                        .get(Object[].class);
		if (d.isPresent())
		{
			Object[] dataObject = d.get();
			byte[] data = (byte[]) dataObject[0];
			if (flushToDisk && dr.isPresent())
			{
				if (flushExploded)
				{
					try
					{
						data = unzip(data);
					}
					catch (com.guicedee.activitymaster.fsdm.client.services.exceptions.ResourceItemException ee)
					{
						//not gzipped just data
					}
					saveDataFile(data, UUID.fromString(dr.get()
					                                     .getId()));
					return data;
				}
				else
				{
					saveDataFile(data, UUID.fromString(dr.get()
					                                     .getId()));
				}
			}
			return unzip(data);
		}
		else
		{
			LogFactory.getLog("ResourceItemDataFetch")
			          .log(Level.FINE, "No resource item data exists");
			return new byte[]{};
		}
	}
	
	@Override
	public String getFilename()
	{
		var dr = getDataRow();
		if (dr.isPresent())
		{
			var r = dr.get();
			var id = r.getId();
			return "data/" + id + (flushExploded ? ".exploded" : ".dat");
		}
		return null;
	}
	
	@Override
	public Optional<IResourceData<?, ?>> getDataRow(java.util.UUID... identityToken)
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
		if ("true".equals(System.getProperty("encrypt", "true")))
		{
			data = new Passwords().integerEncrypt(data)
			                      .getBytes();
		}
		
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
		     org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream gzipOutput = new org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream(baos))
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
	byte[] unzip(byte[] data)
	{
		try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
		     org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream archive = new org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream(bais);
		     ByteArrayOutputStream output = new ByteArrayOutputStream())
		{
			org.apache.commons.compress.utils.IOUtils.copy(archive, output);
			byte[] outcome = output.toByteArray();
			if ("true".equals(System.getProperty("encrypt", "true")))
			{
				outcome = new Passwords().integerDecrypt(new String(outcome));
			}
			return outcome;
		}
		catch (IOException e)
		{
			LogFactory.getLog(getClass()).log(Level.WARNING, "Returning default data, unable to decompress the resource", e);
			return data;
		}
	}
	
	
	//@SuppressWarnings("ResultOfMethodCallIgnored")
	@Override
	public void updateData(byte[] data, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		//	System.out.println(LocalDateTime.now() + " start zip - " + data.length);
		if(data == null || data.length == 0)
		{
			throw new RuntimeException("Cannot store 0 data into a resource item");
		}
		if (!flushToDisk)
		{
			data = zip(data);
		}
		else
		{
			if (!flushExploded)
			{
				data = zip(data);
			}
			else
			{
				//	System.out.println("Using exploded storage");
			}
		}
		//	System.out.println(LocalDateTime.now() + " end zip - " + data.length);
		//	System.out.println(LocalDateTime.now() + " start update");
		if (!flushToDisk)
		{
			//	System.out.println(LocalDateTime.now() + " start search");
			Optional<ResourceItemData> d
					= new ResourceItemData().builder()
					                        .inActiveRange()
					                        //    .inDateRange()
					                        .where(ResourceItemData_.resource, Equals, this)
					                        .latestFirst()
					                        .setReturnFirst(true)
					                        .get();
			//	System.out.println(LocalDateTime.now() + " end search");
			if (d.isPresent())
			{
				ResourceItemData rid = d.get();
				if (data.length == 0)
				{
					throw new ResourceItemException("Cannot create a resource item that has no data?");
				}
				rid.setResourceItemData(data);
				rid.update();
			}
		}
		else
		{
			ResourceItemData rid = (ResourceItemData) getDataRow().orElse(null);
			if (rid != null)
			{
				saveDataFile(data, UUID.fromString(rid.getId()));
				if (flushExploded)
				{
					if(data[0] == 0)
					{
						System.out.println("Corrupted file not writing to DB - " + rid.getId());
						return;
					}
					data = zip(data);
				}
				rid.setResourceItemData(data);
				rid.update();
			}
		}
		//	System.out.println(LocalDateTime.now() + " end update");
	}
	
	private void saveDataFile(byte[] data, UUID rid)
	{
		File directory = new File(flushToDiskLocation);
		if (!directory.exists())
		{
			try
			{
				FileUtils.forceMkdirParent(new File(flushToDiskLocation));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		String filename = FilenameUtils.concat(flushToDiskLocation, rid + (flushExploded ? ".exploded" : ".dat"));
		File dataFile = new File(filename);
		if (!dataFile.exists())
		{
			try
			{
				FileUtils.forceMkdirParent(new File(filename));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		try (FileOutputStream fileWriter = new FileOutputStream(filename))
		{
			fileWriter.write(data);
		}
		catch (IOException e)
		{
			e.printStackTrace();
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
		rid.setWarehouseLastUpdatedTimestamp(EndOfTime.atOffset(java.time.ZoneOffset.UTC));
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
	public void configureNewHierarchyItem(IWarehouseRelationshipClassificationTable<?, ?, ResourceItem, ResourceItem, java.lang.String> newLink, ResourceItem parent, ResourceItem child, String value)
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
