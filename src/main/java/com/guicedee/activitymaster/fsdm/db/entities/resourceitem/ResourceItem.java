package com.guicedee.activitymaster.fsdm.db.entities.resourceitem;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceData;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItemType;
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
import com.guicedee.activitymaster.fsdm.systems.ActiveFlagSystem;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.entityassist.enumerations.Operand.Equals;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD.convertToUTCDateTime;
import static jakarta.persistence.FetchType.EAGER;

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
@Log4j2
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceItem
    extends WarehouseSCDTable<ResourceItem, ResourceItemQueryBuilder, UUID, ResourceItemSecurityToken>
    implements IResourceItem<ResourceItem, ResourceItemQueryBuilder>
{
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @Column(nullable = false,
      name = "ResourceItemID")
  @JsonValue
  private UUID id;
  @Basic(optional = false,
      fetch = EAGER)
  @Column(nullable = false,
      name = "ResourceItemDataType",
      length = 150)
  @Size(max = 150)
  private String resourceItemDataType;

  @OneToMany(
      mappedBy = "resourceItemID",
      fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  private List<ResourceItemXClassification> classifications;

  @OneToMany(
      mappedBy = "resourceItemID",
      fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  private List<ResourceItemXResourceItemType> types;

  @OneToMany(
      mappedBy = "resourceItemID",
      fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  private List<InvolvedPartyXResourceItem> parties;
  @OneToMany(
      mappedBy = "resourceItemID",
      fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  private List<ArrangementXResourceItem> arrangements;

  @OneToMany(
      mappedBy = "resource",
      fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  private List<ResourceItemData> data;

  @OneToMany(
      mappedBy = "resourceItemID",
      fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  private List<AddressXResourceItem> addresses;
  @OneToMany(
      mappedBy = "resourceItemID",
      fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  private List<EventXResourceItem> events;
  @OneToMany(
      mappedBy = "resourceItemID",
      fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  private List<ProductXResourceItem> products;

  @OneToMany(
      mappedBy = "resourceItemID",
      fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  private List<ClassificationDataConceptXResourceItem> concept;
  @OneToMany(
      mappedBy = "resourceItemID",
      fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  private List<ClassificationXResourceItem> classificationXResourceItemList;
  @OneToMany(
      mappedBy = "base",
      fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  private List<ResourceItemSecurityToken> securities;
  @OneToMany(
      mappedBy = "resourceItemID",
      fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  private List<GeographyXResourceItem> geographies;


  @Override
  public void configureSecurityEntity(ResourceItemSecurityToken securityEntity)
  {
    securityEntity.setBase(this);
  }

  @Override
  public Uni<IResourceItem<?, ?>> updateDataTypeValue(Mutiny.Session session, String newValue)
  {
    setResourceItemDataType(newValue);
    return builder(session).find(getId())
               .update()
               .replaceWith(Uni.createFrom()
                                .item(this));
  }

  @Override
  public Uni<byte[]> getData(Mutiny.Session session, UUID... identityToken)
  {
    ResourceItemData rid = new ResourceItemData();
    return rid.builder(session)
               .inActiveRange()
               .inDateRange()
               .where(ResourceItemData_.resource, Equals, this)
               .latestFirst()
               .setReturnFirst(true)
               .selectColumn(ResourceItemData_.resourceItemData)
               .get(byte[].class)
               .onItem()
               .ifNotNull()
               .transform(data -> unzip(data))
               .onItem()
               .ifNull()
               .continueWith(() -> {
                log.error("No resource item data exists");
                return new byte[]{};
              });
  }

  @Override
  public Uni<String> getFilename(Mutiny.Session session)
  {
    ResourceItemData rid = new ResourceItemData();
    return rid.builder(session)
               .inActiveRange()
               .inDateRange()
               .where(ResourceItemData_.resource, Equals, this)
               .get()
               .onItem()
               .ifNotNull()
               .transform(r -> "data/" + r.getId() + ".dat")
               .onItem()
               .ifNull()
               .continueWith(() -> null);
  }

  @Override
  public Uni<IResourceData<?, ?, ?>> getDataRow(Mutiny.Session session, UUID... identityToken)
  {
    ResourceItemData rid = new ResourceItemData();
    return rid.builder(session)
               .inActiveRange()
               .inDateRange()
               .where(ResourceItemData_.resource, Equals, this)
               .get()
               .map(ridd -> ridd);
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


  @Override
  public Uni<Void> updateData(Mutiny.Session session, byte[] data, ISystems<?, ?> system, UUID... identityToken)
  {
    if (data == null || data.length == 0)
    {
      return Uni.createFrom()
                 .failure(new RuntimeException("Cannot store 0 data into a resource item"));
    }
    ResourceItemData rid = new ResourceItemData();
    return rid.builder(session)
               .inActiveRange()
               .where(ResourceItemData_.resource, Equals, this)
               .latestFirst()
               .setReturnFirst(true)
               .get()
               .chain(resourceItemData -> {
                 resourceItemData.setResourceItemData(data);
                 return session.merge(resourceItemData);
               })
               .replaceWithVoid();
  }

  @Override
  public Uni<Void> updateAndKeepHistoryData(Mutiny.Session session, byte[] data, ISystems<?, ?> system, UUID... identityToken)
  {
    final byte[] zippedData = zip(data);

    ResourceItemData rid = new ResourceItemData();
    return rid.builder(session)
               .inActiveRange()
               .where(ResourceItemData_.resource, Equals, this)
               .latestFirst()
               .setReturnFirst(true)
               .get()
               .onItem()
               .ifNotNull()
               .invoke(resourceItemData -> {
                 resourceItemData.archive(session);
               })
               .onItem()
               .transformToUni(item -> {
                 ResourceItemData newRid = new ResourceItemData();
                 newRid.setResource(this);
                 newRid.setEffectiveFromDate(convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
                 newRid.setWarehouseCreatedTimestamp(convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
                 newRid.setEffectiveToDate(EndOfTime.atOffset(java.time.ZoneOffset.UTC));
                 newRid.setWarehouseLastUpdatedTimestamp(convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
                 newRid.setResourceItemData(zippedData);
                 newRid.setActiveFlagID(getActiveFlagID());
                 newRid.setOriginalSourceSystemID(getSystemID());
                 newRid.setSystemID(getSystemID());
                 newRid.setEnterpriseID(system.getEnterpriseID());
                 return session.persist(newRid);
               });
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

  /**
   * Archives the entity by setting its active flag to archived.
   * This method is reactive as it performs a database operation.
   *
   * @return A Uni that completes when the archiving is done
   */
  @SuppressWarnings("unchecked")
  public Uni<ResourceItem> archive(Mutiny.Session session)
  {
    IEnterprise<?, ?> enterprise = getEnterpriseID();
    ActiveFlagSystem activeSystem = com.guicedee.client.IGuiceContext.get(ActiveFlagSystem.class);
    return activeSystem.getSystemToken(session, enterprise)
               .chain(systemToken -> {
                 return com.guicedee.client.IGuiceContext.get(IActiveFlagService.class)
                            .getArchivedFlag(session, enterprise, systemToken)
                            .chain(archivedFlag -> {
                              setActiveFlagID((IActiveFlag<?, ?>) archivedFlag);
                              return session.merge(this);
                            });
               });
  }

  @Override
  public Uni<String> getResourceItemDataType()
  {
    return Uni.createFrom()
               .item(this.resourceItemDataType);
  }

  public ResourceItem setResourceItemDataType(@Size(max = 150) String resourceItemDataType)
  {
    this.resourceItemDataType = resourceItemDataType;
    return this;
  }

  @Override
  public void configureNewHierarchyItem(IWarehouseRelationshipClassificationTable<?, ?, ResourceItem, ResourceItem, UUID, ?> newLink, ResourceItem parent, ResourceItem child, String value)
  {
    ResourceItemXResourceItem ri = (ResourceItemXResourceItem) newLink;
    ri.setParentResourceItemID(parent);
    ri.setChildResourceItemID(child);
    ri.setValue(value);
  }

  @Override
  public void configureForClassification(Mutiny.Session session, IWarehouseRelationshipClassificationTable linkTable, IClassification<?, ?> classificationValue, ISystems<?, ?> system)
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
