package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXResourceItemQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
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
@Table(schema = "Arrangement", name = "ArrangementXResourceItem")
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
public class ArrangementXResourceItem
        extends WarehouseClassificationRelationshipTable<Arrangement,
        ResourceItem,
        ArrangementXResourceItem,
        ArrangementXResourceItemQueryBuilder,
        UUID,
        ArrangementXResourceItemSecurityToken
        >
        implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "ArrangementXResourceItemID")

    private java.util.UUID id;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ArrangementXResourceItemSecurityToken> arrangementXResourceItemSecurityTokenList;

    @JoinColumn(name = "ArrangementID",
            referencedColumnName = "ArrangementID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)

    private Arrangement arrangementID;

    @JoinColumn(name = "ResourceItemID",
            referencedColumnName = "ResourceItemID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)

    private ResourceItem resourceItemID;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ArrangementXResourceItemSecurityToken> securities;

    @Override
    public void configureSecurityEntity(ArrangementXResourceItemSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }


    public ArrangementXResourceItem setArrangementXResourceItemSecurityTokenList(List<ArrangementXResourceItemSecurityToken> arrangementXResourceItemSecurityTokenList)
    {
        this.arrangementXResourceItemSecurityTokenList = arrangementXResourceItemSecurityTokenList;
        return this;
    }

    public ArrangementXResourceItem setArrangementID(Arrangement arrangementID)
    {
        this.arrangementID = arrangementID;
        return this;
    }

    public ArrangementXResourceItem setResourceItemID(ResourceItem resourceItemID)
    {
        this.resourceItemID = resourceItemID;
        return this;
    }

    public ArrangementXResourceItem setSecurities(List<ArrangementXResourceItemSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    @Override
    public Arrangement getPrimary()
    {
        return getArrangementID();
    }

    @Override
    public ResourceItem getSecondary()
    {
        return getResourceItemID();
    }

    public List<ArrangementXResourceItemSecurityToken> getArrangementXResourceItemSecurityTokenList()
    {
        return arrangementXResourceItemSecurityTokenList;
    }

    public Arrangement getArrangementID()
    {
        return arrangementID;
    }

    public ResourceItem getResourceItemID()
    {
        return resourceItemID;
    }

    public List<ArrangementXResourceItemSecurityToken> getSecurities()
    {
        return securities;
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
        ArrangementXResourceItem that = (ArrangementXResourceItem) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(getId());
    }
}
