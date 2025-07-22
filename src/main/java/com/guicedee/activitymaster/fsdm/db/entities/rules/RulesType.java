package com.guicedee.activitymaster.fsdm.db.entities.rules;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRulesType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.rules.builders.RulesTypeQueryBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(schema = "Rules",
        name = "RulesType")
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
public class RulesType
        extends WarehouseSCDTable<RulesType, RulesTypeQueryBuilder, UUID, RulesTypeSecurityToken>
        implements IRulesType<RulesType, RulesTypeQueryBuilder>,
        IWarehouseNameAndDescriptionTable<RulesType, RulesTypeQueryBuilder, UUID>
{
    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "RulesTypeID")
    @JsonValue

    private java.util.UUID id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1,
            max = 200)
    @Column(nullable = false,
            length = 200,
            name = "RulesTypeName")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1,
            max = 200)
    @Column(nullable = false,
            length = 200,
            name = "RulesTypeDesc")
    private String description;

    @OneToMany(
            mappedBy = "rulesTypeID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<RulesXRulesType> rulesXRulesTypeList;
    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<RulesTypeSecurityToken> securities;
    @OneToMany(
            mappedBy = "rulesTypeID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<RulesTypeXResourceItem> resources;

    @OneToMany(
            mappedBy = "rulesTypeID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<RulesTypeXClassification> classifications;


    public RulesType(UUID rulesTypeID, String rulesTypName, String rulesTypeDesc)
    {
        this.id = rulesTypeID;
        this.name = rulesTypName;
        this.description = rulesTypeDesc;
    }

    @Override
    public void configureSecurityEntity(RulesTypeSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<RulesXRulesType> getRulesXRulesTypeList()
    {
        return this.rulesXRulesTypeList;
    }

    public RulesType setRulesXRulesTypeList(List<RulesXRulesType> rulesXRulesTypeList)
    {
        this.rulesXRulesTypeList = rulesXRulesTypeList;
        return this;
    }

    public List<RulesTypeSecurityToken> getSecurities()
    {
        return this.securities;
    }

    public RulesType setSecurities(List<RulesTypeSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public List<RulesTypeXClassification> getClassifications()
    {
        return classifications;
    }

    public void setClassifications(List<RulesTypeXClassification> classifications)
    {
        this.classifications = classifications;
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
        RulesType rulesType = (RulesType) o;
        return Objects.equals(getName(), rulesType.getName());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }

    @Override
    public String toString()
    {
        return getName();
    }

    @Override
    public @NotNull @Size(min = 1,
            max = 200) String getName()
    {
        return this.name;
    }

    @Override
    public RulesType setName(@NotNull @Size(min = 1,
            max = 200) String name)
    {
        this.name = name;
        return this;
    }

    @Override
    public @NotNull @Size(min = 1,
            max = 200) String getDescription()
    {
        return this.description;
    }

    @Override
    public RulesType setDescription(@NotNull @Size(min = 1,
            max = 200) String description)
    {
        this.description = description;
        return this;
    }


    @Override
    public void configureForClassification(IWarehouseRelationshipClassificationTable linkTable, IClassification<?, ?> classificationValue, ISystems<?, ?> system)
    {
        RulesTypeXClassification r = (RulesTypeXClassification) linkTable;
        r.setRulesTypeID(this);
    }

    @Override
    public void configureResourceItemAddable(IWarehouseRelationshipTable linkTable, RulesType primary, IResourceItem<?, ?> secondary, IClassification<?, ?> classificationValue, String value, IEnterprise<?,?> enterprise)
    {
        RulesTypeXResourceItem r = (RulesTypeXResourceItem) linkTable;
        r.setRulesTypeID(primary);
        r.setResourceItemID((ResourceItem) secondary);
        r.setClassificationID(classificationValue);
        r.setValue(value);
    }

}
