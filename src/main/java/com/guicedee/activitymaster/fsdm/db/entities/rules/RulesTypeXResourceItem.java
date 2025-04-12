package com.guicedee.activitymaster.fsdm.db.entities.rules;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.rules.builders.RulesTypeXResourceItemQueryBuilder;
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
@Entity
@Table(schema = "Rules",
        name = "RulesTypeXResourceItem")
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
public class RulesTypeXResourceItem
        extends WarehouseClassificationRelationshipTable<RulesType,
        ResourceItem,
        RulesTypeXResourceItem,
        RulesTypeXResourceItemQueryBuilder,
        UUID,
        RulesTypeXResourceItemSecurityToken>
        implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @Column(nullable = false,
            name = "RulesTypeXResourceItemID")

    private java.util.UUID id;
    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<RulesTypeXResourceItemSecurityToken> securities;
    @JoinColumn(name = "RulesTypeID",
            referencedColumnName = "RulesTypeID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private RulesType rulesTypeID;
    @JoinColumn(name = "ResourceItemID",
            referencedColumnName = "ResourceItemID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private ResourceItem resourceItemID;

    @Override
    public void configureSecurityEntity(RulesTypeXResourceItemSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<RulesTypeXResourceItemSecurityToken> getSecurities()
    {
        return securities;
    }

    public RulesTypeXResourceItem setSecurities(List<RulesTypeXResourceItemSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public RulesType getRulesTypeID()
    {
        return rulesTypeID;
    }

    public RulesTypeXResourceItem setRulesTypeID(RulesType rulesTypeID)
    {
        this.rulesTypeID = rulesTypeID;
        return this;
    }

    public ResourceItem getResourceItemID()
    {
        return resourceItemID;
    }

    public RulesTypeXResourceItem setResourceItemID(ResourceItem resourceItemID)
    {
        this.resourceItemID = resourceItemID;
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
        RulesTypeXResourceItem that = (RulesTypeXResourceItem) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }

    @Override
    public RulesType getPrimary()
    {
        return getRulesTypeID();
    }

    @Override
    public ResourceItem getSecondary()
    {
        return getResourceItemID();
    }
}
