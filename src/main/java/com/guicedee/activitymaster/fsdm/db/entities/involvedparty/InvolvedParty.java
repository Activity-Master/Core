package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProductType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRules;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.IdentificationTypes;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.Address;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.ArrangementXInvolvedParty;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXInvolvedParty;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductType;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.rules.Rules;
import com.guicedee.activitymaster.fsdm.systems.InvolvedPartySystem;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.logging.Logger;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.client.IGuiceContext.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings({"unused", "rawtypes"})
@Entity
@Table(schema = "Party",
        name = "InvolvedParty")
@XmlRootElement
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvolvedParty
        extends WarehouseSCDTable<InvolvedParty, InvolvedPartyQueryBuilder, UUID,
        InvolvedPartySecurityToken
        >
        implements IInvolvedParty<InvolvedParty, InvolvedPartyQueryBuilder>
{
    private static final Logger log = Logger.getLogger(InvolvedParty.class.getName());
    @Id

    @Column(nullable = false,
            name = "InvolvedPartyID")
    @JsonValue

    private java.util.UUID id;

    @OneToMany(
            mappedBy = "involvedPartyID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<InvolvedPartyXClassification> classifications;
    @OneToMany(
            mappedBy = "involvedPartyID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<InvolvedPartyXInvolvedPartyNameType> names;
    @OneToMany(
            mappedBy = "involvedPartyID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<InvolvedPartyXResourceItem> resources;
    @OneToMany(
            mappedBy = "involvedPartyID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ArrangementXInvolvedParty> arrangements;
    @OneToMany(
            mappedBy = "involvedPartyID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<EventXInvolvedParty> events;
    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<InvolvedPartySecurityToken> securities;
    @OneToMany(
            mappedBy = "childInvolvedPartyID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<InvolvedPartyXInvolvedParty> involvedPartyXInvolvedPartyList;
    @OneToMany(
            mappedBy = "parentInvolvedPartyID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<InvolvedPartyXInvolvedParty> involvedPartyXInvolvedPartyList1;
    @OneToMany(
            mappedBy = "involvedPartyID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<InvolvedPartyXInvolvedPartyType> types;
    @OneToMany(
            mappedBy = "involvedPartyID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<InvolvedPartyXProduct> products;
    @OneToMany(
            mappedBy = "involvedPartyID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<InvolvedPartyXAddress> addresses;
    @OneToMany(
            mappedBy = "involvedPartyID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<InvolvedPartyXInvolvedPartyIdentificationType> identities;

    @Override
    public UUID getSecurityIdentity()
    {
        IInvolvedPartyService<?> partyService = get(IInvolvedPartyService.class);
        String value = this.findInvolvedPartyIdentificationType(NoClassification, IdentificationTypes.IdentificationTypeUUID, null, getSystemID(), true, true,
                        get(InvolvedPartySystem.class).getSystemToken(getEnterprise()))
                .orElseThrow()
                .getValue();
        return UUID.fromString(value);
    }

    @Override
    public void configureSecurityEntity(InvolvedPartySecurityToken securityEntity)
    {
        securityEntity.setBase(this);
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
        InvolvedParty that = (InvolvedParty) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public String toString()
    {
        return getId() == null ? "" : getId().toString();
    }

    @Override
    public void configureNewHierarchyItem(IWarehouseRelationshipClassificationTable<?, ?, InvolvedParty, InvolvedParty, UUID, ?> newLink, InvolvedParty parent, InvolvedParty child, String value)
    {
        InvolvedPartyXInvolvedParty i = (InvolvedPartyXInvolvedParty) newLink;
        i.setParentInvolvedPartyID(parent);
        i.setChildInvolvedPartyID(child);
        i.setValue(Strings.nullToEmpty(value));
    }

    @Override
    public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable, IClassification<?, ?> classificationValue, ISystems<?, ?> system)
    {
        InvolvedPartyXClassification i = (InvolvedPartyXClassification) linkTable;
        i.setInvolvedPartyID(this);
    }

    @Override
    public void configureInvolvedPartyIdentificationTypeAddable(IWarehouseRelationshipTable linkTable, InvolvedParty primary, IInvolvedPartyIdentificationType<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
    {
        InvolvedPartyXInvolvedPartyIdentificationType i = (InvolvedPartyXInvolvedPartyIdentificationType) linkTable;
        i.setInvolvedPartyID(primary);
        i.setInvolvedPartyIdentificationTypeID((InvolvedPartyIdentificationType) secondary);
        i.setClassificationID(classificationValue);
        i.setValue(value);
    }

    @Override
    public void configureInvolvedPartyNameTypeAddable(IWarehouseRelationshipTable linkTable, InvolvedParty primary, IInvolvedPartyNameType<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
    {
        InvolvedPartyXInvolvedPartyNameType i = (InvolvedPartyXInvolvedPartyNameType) linkTable;
        i.setInvolvedPartyID(primary);
        i.setInvolvedPartyNameTypeID((InvolvedPartyNameType) secondary);
        i.setClassificationID(classificationValue);
        i.setValue(value);
    }

    @Override
    public void configureInvolvedPartyTypeAddable(IWarehouseRelationshipTable linkTable, InvolvedParty primary, IInvolvedPartyType<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
    {
        InvolvedPartyXInvolvedPartyType i = (InvolvedPartyXInvolvedPartyType) linkTable;
        i.setInvolvedPartyID(primary);
        i.setInvolvedPartyTypeID((InvolvedPartyType) secondary);
        i.setClassificationID(classificationValue);
        i.setValue(value);
    }

    @Override
    public void configureProductTypeLinkValue(IWarehouseRelationshipTable linkTable, InvolvedParty primary, IProductType<?, ?> secondary, IClassification<?, ?> classificationValue, String value, IEnterprise<?, ?> enterprise)
    {
        InvolvedPartyXProductType p = (InvolvedPartyXProductType) linkTable;
        p.setInvolvedPartyID(primary);
        p.setProductTypeID((ProductType) secondary);
        p.setClassificationID(classificationValue);
        p.setValue(value);

    }

    @Override
    public void configureProductAddable(IWarehouseRelationshipTable linkTable, InvolvedParty primary, IProduct<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
    {
        InvolvedPartyXProduct p = (InvolvedPartyXProduct) linkTable;
        p.setInvolvedPartyID(primary);
        p.setProductID((Product) secondary);
        p.setClassificationID(classificationValue);
        p.setValue(value);
    }

    @Override
    public void configureResourceItemAddable(IWarehouseRelationshipTable linkTable, InvolvedParty primary, IResourceItem<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
    {
        InvolvedPartyXResourceItem r = (InvolvedPartyXResourceItem) linkTable;
        r.setInvolvedPartyID(primary);
        r.setResourceItemID((ResourceItem) secondary);
        r.setClassificationID(classificationValue);
        r.setValue(value);
    }

    @Override
    public void configureAddressLinkValue(IWarehouseRelationshipTable linkTable, InvolvedParty primary, IAddress<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
    {
        InvolvedPartyXAddress a = (InvolvedPartyXAddress) linkTable;
        a.setInvolvedPartyID(primary);
        a.setAddressID((Address) secondary);
        a.setClassificationID(classificationValue);
        a.setValue(value);
    }

    @Override
    public void configureRulesAddable(IWarehouseRelationshipTable linkTable, InvolvedParty primary, IRules<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
    {
        InvolvedPartyXRules r = (InvolvedPartyXRules) linkTable;
        r.setInvolvedPartyID(primary);
        r.setRulesID((Rules) secondary);
        r.setClassificationID(classificationValue);
        r.setValue(value);
    }
}
