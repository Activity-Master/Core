package com.guicedee.activitymaster.fsdm.db.entities.resourceitem;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemXResourceItemQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
@Table(schema = "Resource",
        name = "ResourceItemXResourceItem")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
public class ResourceItemXResourceItem
        extends WarehouseClassificationRelationshipTable<ResourceItem,
                                ResourceItem,
                                ResourceItemXResourceItem,
                                ResourceItemXResourceItemQueryBuilder,
                                UUID,
                                ResourceItemXResourceItemSecurityToken>
        implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "ResourceItemXResourceItemID")

    private java.util.UUID id;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ResourceItemXResourceItemSecurityToken> securities;

    @JoinColumn(name = "ChildResourceItemID",
            referencedColumnName = "ResourceItemID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private ResourceItem childResourceItemID;
    @JoinColumn(name = "ParentResourceItemID",
            referencedColumnName = "ResourceItemID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private ResourceItem parentResourceItemID;

    @Override
    public void configureSecurityEntity(ResourceItemXResourceItemSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<ResourceItemXResourceItemSecurityToken> getSecurities()
    {
        return securities;
    }

    public ResourceItemXResourceItem setSecurities(List<ResourceItemXResourceItemSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public ResourceItem getChildResourceItemID()
    {
        return childResourceItemID;
    }

    public ResourceItemXResourceItem setChildResourceItemID(ResourceItem childResourceItemID)
    {
        this.childResourceItemID = childResourceItemID;
        return this;
    }

    public ResourceItem getParentResourceItemID()
    {
        return parentResourceItemID;
    }

    public ResourceItemXResourceItem setParentResourceItemID(ResourceItem parentResourceItemID)
    {
        this.parentResourceItemID = parentResourceItemID;
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
        ResourceItemXResourceItem that = (ResourceItemXResourceItem) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }

    @Override
    public ResourceItem getPrimary()
    {
        return getParentResourceItemID();
    }

    @Override
    public ResourceItem getSecondary()
    {
        return getChildResourceItemID();
    }
}
