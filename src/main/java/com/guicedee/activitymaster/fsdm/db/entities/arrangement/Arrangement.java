package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangement;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangementType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRules;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRulesType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXArrangement;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.rules.Rules;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesType;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings({"unused", "rawtypes"})
@Entity
@Table(schema = "Arrangement",
        name = "Arrangement")
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
public class Arrangement
        extends WarehouseSCDTable<Arrangement, ArrangementQueryBuilder, UUID, ArrangementSecurityToken>
        implements IArrangement<Arrangement, ArrangementQueryBuilder>
{
    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "ArrangementID")
    @JsonValue

    private java.util.UUID id;


    @OneToMany(
            mappedBy = "arrangementID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ArrangementXClassification> classifications;


    @OneToMany(
            mappedBy = "arrangementID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ArrangementXInvolvedParty> parties;

    @OneToMany(
            mappedBy = "arrangementID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ArrangementXResourceItem> resources;

    @OneToMany(
            mappedBy = "arrangementID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<EventXArrangement> events;


    @OneToMany(
            mappedBy = "childArrangementID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ArrangementXArrangement> arrangementXArrangementList;

    @OneToMany(
            mappedBy = "parentArrangementID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ArrangementXArrangement> arrangementXArrangementList1;

    @OneToMany(
            mappedBy = "arrangementID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ArrangementXProduct> products;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ArrangementSecurityToken> securities;


    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "arrangement")
    private List<ArrangementXArrangementType> types;
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "arrangement")
    private List<ArrangementXRules> rules;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "arrangement")
    private List<ArrangementXRulesType> ruleTypes;

    @Override
    public void configureSecurityEntity(ArrangementSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public void setMyInvolvedPartyLinkValue(ArrangementXInvolvedParty classificationLink, InvolvedParty involvedParty, IEnterprise<?, ?> enterprise)
    {
        classificationLink.setArrangementID(this);
        classificationLink.setInvolvedPartyID(involvedParty);
    }


    /**
     * Removes this arrangement and all its related entities.
     * This method performs actions and returns a Uni that completes when the removal is done.
     * It returns the result of calling super.remove().
     *
     * @return A Uni that completes when the removal is done
     */
    @Override
    public Uni<Arrangement> remove()
    {
        // First remove all child entities
        if (classifications != null)
        {
            for (ArrangementXClassification classification : classifications)
            {
                classification.remove();
            }
        }
        if (products != null)
        {
            for (ArrangementXProduct product : products)
            {
                product.remove();
            }
        }
        if (securities != null)
        {
            for (ArrangementSecurityToken security : securities)
            {
                security.remove();
            }
        }
        if (types != null)
        {
            for (ArrangementXArrangementType type : types)
            {
                type.remove();
            }
        }
        if (arrangementXArrangementList != null)
        {
            for (ArrangementXArrangement arrangementXArrangement : arrangementXArrangementList)
            {
                arrangementXArrangement.remove();
            }
        }
        if (arrangementXArrangementList1 != null)
        {
            for (ArrangementXArrangement arrangementXArrangement : arrangementXArrangementList1)
            {
                arrangementXArrangement.remove();
            }
        }

        // Then call super.remove() which returns a Uni
        return super.remove();
    }

    public Arrangement setClassifications(List<ArrangementXClassification> classifications)
    {
        this.classifications = classifications;
        return this;
    }

    public Arrangement setParties(List<ArrangementXInvolvedParty> parties)
    {
        this.parties = parties;
        return this;
    }

    public Arrangement setResources(List<ArrangementXResourceItem> resources)
    {
        this.resources = resources;
        return this;
    }

    public Arrangement setEvents(List<EventXArrangement> events)
    {
        this.events = events;
        return this;
    }

    public Arrangement setArrangementXArrangementList(List<ArrangementXArrangement> arrangementXArrangementList)
    {
        this.arrangementXArrangementList = arrangementXArrangementList;
        return this;
    }

    public Arrangement setArrangementXArrangementList1(List<ArrangementXArrangement> arrangementXArrangementList1)
    {
        this.arrangementXArrangementList1 = arrangementXArrangementList1;
        return this;
    }

    public Arrangement setProducts(List<ArrangementXProduct> products)
    {
        this.products = products;
        return this;
    }

    public Arrangement setSecurities(List<ArrangementSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public Arrangement setTypes(List<ArrangementXArrangementType> types)
    {
        this.types = types;
        return this;
    }

    @Override
    public String toString()
    {
        return getId() + "";
    }

    @Override
    public void configureNewHierarchyItem(IWarehouseRelationshipClassificationTable<?, ?, Arrangement, Arrangement, UUID, ?> newLink, Arrangement parent, Arrangement child, String value)
    {
        ArrangementXArrangement axa = (ArrangementXArrangement) newLink;
        axa.setParentArrangementID(parent);
        axa.setChildArrangementID(child);
        axa.setValue(value);
    }

    @Override
    public void configureArrangementTypeAddable(IWarehouseRelationshipTable linkTable, Arrangement primary, IArrangementType<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
    {
        ArrangementXArrangementType axa = (ArrangementXArrangementType) linkTable;
        axa.setArrangement(primary);
        axa.setType((ArrangementType) secondary);
        axa.setClassificationID(classificationValue);
        axa.setValue(value);
    }

    @Override
    public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable, IClassification<?, ?> classificationValue, ISystems<?, ?> system)
    {
        ((ArrangementXClassification) linkTable).setArrangementID(this);
    }

    @Override
    public void configureProductAddable(IWarehouseRelationshipTable linkTable, Arrangement primary, IProduct<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
    {
        ArrangementXProduct axa = (ArrangementXProduct) linkTable;
        axa.setArrangementID(primary);
        axa.setProductID((Product) secondary);
        axa.setClassificationID(classificationValue);
        axa.setValue(value);
    }

    @Override
    public void configureRuleTypeLinkValue(IWarehouseRelationshipTable linkTable, Arrangement primary, IRulesType<?, ?> secondary, IClassification<?, ?> classificationValue, String value, IEnterprise<?, ?> enterprise)
    {
        ArrangementXRulesType axa = (ArrangementXRulesType) linkTable;
        axa.setArrangement(primary);
        axa.setRulesTypeID((RulesType) secondary);
        axa.setClassificationID(classificationValue);
        axa.setValue(value);
    }

    @Override
    public void configureRulesAddable(IWarehouseRelationshipTable linkTable, Arrangement primary, IRules<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
    {
        ArrangementXRules axa = (ArrangementXRules) linkTable;
        axa.setArrangement(primary);
        axa.setRulesID((Rules) secondary);
        axa.setClassificationID(classificationValue);
        axa.setValue(value);
    }

    @Override
    public void configureResourceItemAddable(IWarehouseRelationshipTable linkTable, Arrangement primary, IResourceItem<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
    {
        ArrangementXResourceItem axr = (ArrangementXResourceItem) linkTable;
        ((ArrangementXResourceItem) linkTable).setArrangementID(primary);
        ((ArrangementXResourceItem) linkTable).setResourceItemID((ResourceItem) secondary);
        axr.setClassificationID(classificationValue);
        linkTable.setValue(value);
    }

    @Override
    public void configureInvolvedPartyAddable(IWarehouseRelationshipTable linkTable, Arrangement primary, IInvolvedParty<?, ?> secondary, IClassification<?, ?> classificationValue, String value, ISystems<?, ?> system)
    {
        ArrangementXInvolvedParty axi = (ArrangementXInvolvedParty) linkTable;
        axi.setArrangementID(primary);
        axi.setInvolvedPartyID((InvolvedParty) secondary);
        axi.setClassificationID(classificationValue);
        axi.setValue(value);
    }

    public List<ArrangementXClassification> getClassifications()
    {
        return classifications;
    }

    public List<ArrangementXInvolvedParty> getParties()
    {
        return parties;
    }

    public List<ArrangementXResourceItem> getResources()
    {
        return resources;
    }

    public List<EventXArrangement> getEvents()
    {
        return events;
    }

    public List<ArrangementXArrangement> getArrangementXArrangementList()
    {
        return arrangementXArrangementList;
    }

    public List<ArrangementXArrangement> getArrangementXArrangementList1()
    {
        return arrangementXArrangementList1;
    }

    public List<ArrangementXProduct> getProducts()
    {
        return products;
    }

    public List<ArrangementSecurityToken> getSecurities()
    {
        return securities;
    }

    public List<ArrangementXArrangementType> getTypes()
    {
        return types;
    }

    public List<ArrangementXRules> getRules()
    {
        return rules;
    }

    public Arrangement setRules(List<ArrangementXRules> rules)
    {
        this.rules = rules;
        return this;
    }

    public List<ArrangementXRulesType> getRuleTypes()
    {
        return ruleTypes;
    }

    public Arrangement setRuleTypes(List<ArrangementXRulesType> ruleTypes)
    {
        this.ruleTypes = ruleTypes;
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
        Arrangement that = (Arrangement) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(getId());
    }
}
